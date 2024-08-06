import {Box} from "@mui/material";
import pauseBanner from "/Pause_Banner.png";

function Landingpage() {

    return (
        <Box justifyContent={"center"} display={"flex"}>
            <img src={pauseBanner} alt="Pause Banner" style={{maxWidth: '100%', height: 'auto'}}/>
        </Box>
    )
}

export default Landingpage