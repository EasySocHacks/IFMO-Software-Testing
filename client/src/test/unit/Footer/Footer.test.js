import Footer from "../../../components/Footer/Footer";
import {render} from "@testing-library/react";

test("render footer title", () => {
    render(<Footer/>);
    const footer = document.getElementsByClassName("footer")[0];
    expect(footer.innerHTML).toEqual("Created by <a href=\"https://github.com/EasySocHacks\">EasySocHacks</a>")
});