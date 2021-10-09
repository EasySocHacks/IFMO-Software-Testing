import "./Footer.css"

export default function Footer() {
    return (
        <div className="footer">
            Created by <a href={process.env.REACT_APP_CREATOR_GITHUB_LINK}>EasySocHacks</a>
        </div>
    );
}