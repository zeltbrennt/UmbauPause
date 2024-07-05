import {Container} from "@mui/material";
import ClippedDrawer from "./components/ClippedDrawer.tsx";
import {ThemeProvider} from "@emotion/react";
import {lightTheme} from "./Themes.ts";

function App() {

    return (
        <ThemeProvider theme={lightTheme}>
            <Container>
                <ClippedDrawer/>
            </Container>
        </ThemeProvider>
    )
}


export default App
