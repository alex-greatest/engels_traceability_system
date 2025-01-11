import Box from "@mui/system/Box";
import {CircularProgress} from "@mui/material";

export const Loading = () => {
    return(
        <Box sx={{display: 'flex', backgroundColor: '#fff', width: '100%', height: '100vh'}}>
            <CircularProgress sx = {{margin: 'auto auto'}}/>
        </Box>
    );
}
