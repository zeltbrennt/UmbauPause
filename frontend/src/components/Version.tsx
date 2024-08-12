import {Typography} from "@mui/material";

export default function Version({appVersion}) {
    return (
        <div>
            <Typography fontSize={12} fontWeight={"bold"}> Version</Typography>
            <Typography fontSize={10}>{appVersion}</Typography>

        </div>
    )
}