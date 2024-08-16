import {createTheme} from "@mui/material";

export const lightTheme = createTheme({
    palette: {
        mode: 'light',
        primary: {
            main: '#894a67',
            dark: '#390723',
            light: '#ffd8e6',
        },
        secondary: {
            main: '#87d1eb',
            light: '#b6ebff',
            dark: '#05677e',
        },
    },
});