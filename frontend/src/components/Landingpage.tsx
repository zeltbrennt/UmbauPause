import {Box, Container} from "@mui/material";
import pauseBanner from "/Pause_Banner.png";
import Version from "./Version.tsx";
import {useEffect, useState} from "react";
import {BASE_URL} from "../util/constants.ts";

function Landingpage() {

    const [version, setVersion] = useState("unknown") // [1
    useEffect(() => {
        fetch(`${BASE_URL}app-version`)
            .then(response => response.text())
            .then(data => setVersion(data))
    }, [])

    return (
        <Container>
            <Box justifyContent={"center"} display={"flex"}>
                <img src={pauseBanner} alt="Pause Banner" style={{maxWidth: '100%', height: 'auto'}}/>

            </Box>
            <Box position={"fixed"} bottom={0} right={0}>
                <Version appVersion={version}/>
            </Box>
        </Container>
    )
}

export default Landingpage