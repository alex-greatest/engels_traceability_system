import { Signal, useSignal } from '@vaadin/hilla-react-signals';
import React from 'react';
import {
  Dialog,
  DialogContent,
  DialogTitle, Tooltip
} from '@mui/material';
import ComponentNameSetDto from 'Frontend/generated/com/rena/application/entity/dto/component/ComponentNameSetDto';
import SetLayout from 'Frontend/views/components/set/@layout';
import Box from '@mui/system/Box';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';

interface Props {
  dialogOpen: Signal<boolean>;
  updaterComponentNameSet: Signal<(componentNameSet: ComponentNameSetDto) => void>;
}

const ComponentsSetDialog = (props: Props) => {
  const { dialogOpen, updaterComponentNameSet } = props;

  return (
    <Dialog
      fullScreen
      open={dialogOpen.value}
      onClose={() => dialogOpen.value = false}>
      <DialogTitle sx={{display: 'flex'}}>
        Набор компонентов
        <Box sx={{marginLeft: 'auto'}}>
          <IconButton onClick={() => dialogOpen.value = false} aria-label="icon_close_component_set_dialog">
            <Tooltip title="Закрыть">
              <CloseIcon />
            </Tooltip>
          </IconButton>
        </Box>
      </DialogTitle>
      <DialogContent>
              <SetLayout key={"components_set_layout"} func={(componentNameSet: ComponentNameSetDto) => {
                updaterComponentNameSet.value(componentNameSet);
                dialogOpen.value = false;
              }} />
      </DialogContent>
    </Dialog>
  );
}

export default ComponentsSetDialog;