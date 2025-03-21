# Архитектурные паттерны

## Микросервисная структура
```mermaid
graph TD
    A[Frontend] --> B[API Gateway]
    B --> C[Boiler Service]
    B --> D[Component Service]
    B --> E[Report Service]
    
    subgraph Frontend Components
        F[Results View] --> G[Boiler Results]
        G --> H[Order Results]
        G --> I[Operation Results]
        F --> J[Component Results]
    end
```

## Схема данных
```mermaid
erDiagram
    BOILER ||--o{ COMPONENT : contains
    BOILER ||--|{ OPERATION : has
    OPERATION ||--o{ TEST_RESULT : produces
    BOILER }|--|| BOILER_TYPE : has_type
    BOILER }|--|| STATUS : has_status
    BOILER }|--|| STATION : last_station
    OPERATION }|--|| USER : performed_by
```

## Шаблоны проектирования
- Factory Method для создания отчетов и представлений результатов
- Observer для мониторинга статуса операций
- Composite для работы с компонентами и результатами
- Strategy для различных типов фильтрации и отображения
- Repository для доступа к данным

## Ключевые технические решения
- Material React Table для отображения данных с пагинацией
- WebSocket для real-time обновлений
- DTO маппинг через Vaadin Hilla
- Кэширование состояния компонентов
- Типобезопасные API endpoints
