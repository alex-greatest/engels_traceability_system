CREATE OR REPLACE FUNCTION check_station_route(
                p_serial_number VARCHAR,
                p_station_name VARCHAR,
                p_rework_station_name VARCHAR,
                p_ok_status INTEGER
            ) RETURNS TABLE(
                is_ok BOOLEAN,
                message TEXT,
                not_passed_stations VARCHAR[],
                failed_stations VARCHAR[]
            )
            LANGUAGE plpgsql
            AS $$
            DECLARE
                v_current_station_order INTEGER;
                v_rework_date TIMESTAMP;
                v_first_station_after_rework VARCHAR;
                v_first_station_order INTEGER;
                v_operations_history RECORD;
                v_parallel_station_passed BOOLEAN;
                v_passed_station_name VARCHAR;
            BEGIN
                -- Получаем порядковый номер текущей станции
                SELECT sr."order" INTO v_current_station_order
                FROM station_router sr
                JOIN station s ON sr.station_id = s.id
                WHERE s.name = p_station_name;

                -- Получаем всю необходимую информацию об операциях с бойлером в одном запросе
                SELECT
                    (last_op.name = p_rework_station_name) AS last_op_is_rework,
                    target_station.status AS last_station_status,
                    last_rework.date_create AS rework_date,
                    first_after_rework.name AS first_station_after_rework,
                    first_after_rework_order."order" AS first_station_order
                INTO v_operations_history
                FROM (
                    -- Последняя операция для данного бойлера
                    SELECT sh.name, o.date_create
                    FROM operation o
                    JOIN station_history sh ON o.station_history_id = sh.id
                    WHERE o.boiler_serial_number = p_serial_number
                    ORDER BY o.date_create DESC
                    LIMIT 1
                ) AS last_op

                -- Последняя операция на целевой станции (если есть)
                LEFT JOIN LATERAL (
                    SELECT o.status
                    FROM operation o
                    JOIN station_history sh ON o.station_history_id = sh.id
                    WHERE o.boiler_serial_number = p_serial_number
                      AND sh.name = p_station_name
                    ORDER BY o.date_create DESC
                    LIMIT 1
                ) AS target_station ON true

                -- Последняя доработка (если есть)
                LEFT JOIN LATERAL (
                    SELECT o.date_create
                    FROM operation o
                    JOIN station_history sh ON o.station_history_id = sh.id
                    WHERE o.boiler_serial_number = p_serial_number
                      AND sh.name = p_rework_station_name
                    ORDER BY o.date_create DESC
                    LIMIT 1
                ) AS last_rework ON true

                -- Первая станция после доработки (если есть доработка)
                LEFT JOIN LATERAL (
                    SELECT sh.name, o.date_create
                    FROM operation o
                    JOIN station_history sh ON o.station_history_id = sh.id
                    WHERE o.boiler_serial_number = p_serial_number
                      AND last_rework.date_create IS NOT NULL
                      AND o.date_create > last_rework.date_create
                      AND sh.name != p_rework_station_name
                    ORDER BY o.date_create ASC
                    LIMIT 1
                ) AS first_after_rework ON last_rework.date_create IS NOT NULL

                -- Получаем порядковый номер первой станции после доработки
                LEFT JOIN LATERAL (
                    SELECT sr."order"
                    FROM station_router sr
                    JOIN station s ON sr.station_id = s.id
                    WHERE s.name = first_after_rework.name
                ) AS first_after_rework_order ON first_after_rework.name IS NOT NULL;

                -- Проверка 1: Является ли последняя операция доработкой?
                IF v_operations_history.last_op_is_rework THEN
                    RETURN QUERY SELECT
                        true::BOOLEAN,
                        'Разрешено после доработки'::TEXT,
                        ARRAY[]::VARCHAR[],
                        ARRAY[]::VARCHAR[];
                    RETURN;
                END IF;

                -- НОВАЯ ПРОВЕРКА: Проверяем, была ли успешно пройдена любая станция с тем же порядковым номером
                SELECT
                    EXISTS(
                        SELECT 1
                        FROM operation o
                        JOIN station_history sh ON o.station_history_id = sh.id
                        JOIN station s ON sh.name = s.name
                        JOIN station_router sr ON s.id = sr.station_id
                        WHERE o.boiler_serial_number = p_serial_number
                          AND sr."order" = v_current_station_order
                          AND s.name != p_station_name  -- Исключаем текущую станцию
                          AND o.status = p_ok_status
                          AND (v_operations_history.rework_date IS NULL OR o.date_create > v_operations_history.rework_date)
                    ),
                    (
                        SELECT s.name
                        FROM operation o
                        JOIN station_history sh ON o.station_history_id = sh.id
                        JOIN station s ON sh.name = s.name
                        JOIN station_router sr ON s.id = sr.station_id
                        WHERE o.boiler_serial_number = p_serial_number
                          AND sr."order" = v_current_station_order
                          AND s.name != p_station_name  -- Исключаем текущую станцию
                          AND o.status = p_ok_status
                          AND (v_operations_history.rework_date IS NULL OR o.date_create > v_operations_history.rework_date)
                        ORDER BY o.date_create DESC
                        LIMIT 1
                    )
                INTO v_parallel_station_passed, v_passed_station_name;

                IF v_parallel_station_passed THEN
                    RETURN QUERY SELECT
                        false::BOOLEAN,
                        ('Этап уже пройден на параллельной станции ' || v_passed_station_name)::TEXT,
                        ARRAY[]::VARCHAR[],
                        ARRAY[]::VARCHAR[];
                    RETURN;
                END IF;

                -- Проверка 2: Проверка статуса последней операции на указанной станции
                IF v_operations_history.last_station_status IS NOT NULL THEN
                    IF v_operations_history.last_station_status = p_ok_status THEN
                        RETURN QUERY SELECT
                            false::BOOLEAN,
                            ('Тест на станции ' || p_station_name || ' уже пройден успешно')::TEXT,
                            ARRAY[]::VARCHAR[],
                            ARRAY[]::VARCHAR[];
                        RETURN;
                    ELSE
                        RETURN QUERY SELECT
                            true::BOOLEAN,
                            'Разрешен повторный запуск теста'::TEXT,
                            ARRAY[]::VARCHAR[],
                            ARRAY[]::VARCHAR[];
                        RETURN;
                    END IF;
                END IF;

                -- Если есть доработка и после нее нет пройденных станций, разрешаем любую станцию
                IF v_operations_history.rework_date IS NOT NULL AND v_operations_history.first_station_after_rework IS NULL THEN
                    RETURN QUERY SELECT
                        true::BOOLEAN,
                        'Разрешено: первая станция после доработки'::TEXT,
                        ARRAY[]::VARCHAR[],
                        ARRAY[]::VARCHAR[];
                    RETURN;
                END IF;

                -- Определяем начальный порядковый номер для проверки станций
                v_first_station_order := CASE
                    WHEN v_operations_history.first_station_order IS NOT NULL THEN v_operations_history.first_station_order
                    ELSE 0
                END;

                -- Проверка этапов между начальным и текущим
                RETURN QUERY
                WITH orders_to_check AS (
                    -- Получаем все уникальные порядковые номера этапов между начальной и текущей станцией
                    SELECT DISTINCT sr."order"
                    FROM station_router sr
                    JOIN station s ON sr.station_id = s.id
                    WHERE sr."order" >= v_first_station_order
                      AND sr."order" < v_current_station_order
                      AND s.name != p_rework_station_name
                    ORDER BY sr."order"
                ),
                order_operations AS (
                    -- Проверяем для каждого порядкового номера, есть ли хотя бы одна успешная операция
                    SELECT
                        o."order",
                        (
                            SELECT bool_or(op.status = p_ok_status)
                            FROM station_router sr
                            JOIN station s ON sr.station_id = s.id
                            JOIN station_history sh ON s.name = sh.name
                            JOIN operation op ON sh.id = op.station_history_id
                            WHERE sr."order" = o."order"
                              AND op.boiler_serial_number = p_serial_number
                              AND (v_operations_history.rework_date IS NULL OR op.date_create > v_operations_history.rework_date)
                        ) AS order_passed
                    FROM orders_to_check o
                ),
                not_passed_orders AS (
                    -- Порядковые номера этапов, которые не были пройдены успешно
                    SELECT array_agg(o."order") AS orders
                    FROM order_operations o
                    WHERE order_passed IS NULL OR NOT order_passed
                ),
                failed_stations AS (
                    -- Станции, на которых была неудачная операция
                    SELECT array_agg(s.name) AS names
                    FROM operation op
                    JOIN station_history sh ON op.station_history_id = sh.id
                    JOIN station s ON sh.name = s.name
                    JOIN station_router sr ON s.id = sr.station_id
                    WHERE op.boiler_serial_number = p_serial_number
                      AND sr."order" >= v_first_station_order
                      AND sr."order" < v_current_station_order
                      AND op.status != p_ok_status
                      AND (v_operations_history.rework_date IS NULL OR op.date_create > v_operations_history.rework_date)
                ),
                -- Получаем имена станций, представляющих непройденные этапы
                not_passed_stations AS (
                    SELECT array_agg(DISTINCT s.name) AS names
                    FROM station s
                    JOIN station_router sr ON s.id = sr.station_id
                    WHERE sr."order" IN (SELECT unnest(COALESCE((SELECT orders FROM not_passed_orders), ARRAY[]::INTEGER[])))
                    AND s.name != p_rework_station_name
                )
                SELECT
                    COALESCE((SELECT orders FROM not_passed_orders), ARRAY[]::INTEGER[]) = ARRAY[]::INTEGER[],

                    CASE WHEN COALESCE((SELECT orders FROM not_passed_orders), ARRAY[]::INTEGER[]) = ARRAY[]::INTEGER[]
                        THEN 'Все необходимые этапы пройдены успешно'
                        ELSE 'Не все необходимые этапы пройдены успешно'
                    END,

                    COALESCE((SELECT names FROM not_passed_stations), ARRAY[]::VARCHAR[]),
                    COALESCE((SELECT names FROM failed_stations), ARRAY[]::VARCHAR[]);
            END;
            $$;