import React from "react";
import './NotFound.css'

export default function NotFound() {
    return (
        <div className="notFound">
            <h1>{process.env.REACT_APP_NOT_FOUND_TEXT}</h1>
        </div>
    );
}