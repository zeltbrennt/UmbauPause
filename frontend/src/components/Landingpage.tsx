import {Box, Typography} from "@mui/material";

function Landingpage({currentUser}: { currentUser: string | undefined }) {

    return (
        <Box>
            <Typography sx={{typography: {sm: 'h1', xs: 'h3'}}}>
                {currentUser == undefined ? "Hier entsteht in Zukunft eine Website..." : `Willkommen ${currentUser}!`}
            </Typography>
        </Box>
    )
}

export default Landingpage