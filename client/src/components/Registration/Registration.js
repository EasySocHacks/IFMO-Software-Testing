import './Registration.css'

export default function Registration() {
    return (
        <div>
            <iframe name="dummyFrame" id="dummyFrame"/>

            <form method="post"  name="register" autoComplete="off" action="http://localhost:8080/users/register" target="dummyFrame">
                <div className="login-field">
                    <p>Login: </p>
                    <input type="text" name="login"/>
                </div>
                <div className="age-field">
                    <p>Age: </p>
                    <input type="text" name="age"/>
                </div>
                <div className="password-field">
                    <p>Password: </p>
                    <input type="password" name="password"/>
                </div>
                <div className="submit-field">
                    <input type="submit" value="Register"/>
                </div>
            </form>
        </div>
    );
}