import { Signal } from '@vaadin/hilla-react-signals';
import React from 'react';
import {
  Dialog,
  DialogContent,
  DialogTitle, Tooltip
} from '@mui/material';
import Box from '@mui/system/Box';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import BoilerTypeAdditionalDataSetDto
  from 'Frontend/generated/com/rena/application/entity/dto/boiler_type/BoilerTypeAdditionalDataSetDto';
import BoilerTypeDataSetLayout from 'Frontend/views/boiler_type/set/@layout';

interface Props {
  dialogOpen: Signal<boolean>;
  updaterBoilerDataSet: Signal<(boilerTypeDataSet: BoilerTypeAdditionalDataSetDto) => void>;
}

const BoilerTypeAdditionalDialog = (props: Props) => {
  const { dialogOpen, updaterBoilerDataSet } = props;

  return (
    <Dialog
      fullScreen
      open={dialogOpen.value}
      onClose={() => dialogOpen.value = false}>
      <DialogTitle sx={{display: 'flex'}}>
        Набор компонентов
        <Box sx={{marginLeft: 'auto'}}>
          <IconButton onClick={() => dialogOpen.value = false} aria-label="icon_close_boiler_type_additional_set_dialog">
            <Tooltip title="Закрыть">
              <CloseIcon />
            </Tooltip>
          </IconButton>
        </Box>
      </DialogTitle>
      <DialogContent>
              <BoilerTypeDataSetLayout key={"boiler_type_additional_set_layout"} func={(boilerTypeDataSet: BoilerTypeAdditionalDataSetDto) => {
                updaterBoilerDataSet.value(boilerTypeDataSet);
                dialogOpen.value = false;
              }} />
      </DialogContent>
    </Dialog>
  );
}

export default BoilerTypeAdditionalDialog;