import {Box, Container} from "@mui/material";
import ClippedDrawer from "./components/ClippedDrawer.tsx";
import {ThemeProvider} from "@emotion/react";
import {lightTheme} from "./Themes.ts";

function App() {

    return (
        <ThemeProvider theme={lightTheme}>
            <Container>
                <Box>
                    <ClippedDrawer/>
                </Box>
            </Container>
        </ThemeProvider>
    )
}


export default App
