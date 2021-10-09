import Authorization from "../../components/Authorization/Authorization";
import "./AuthorizationPage.css"

export default function AuthorizationPage() {
    return (
        <div className="authPage">
            <h3>Login</h3>
            <Authorization/>
        </div>
    );
}