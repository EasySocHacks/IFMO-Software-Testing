import Footer from "../../../components/Footer/Footer";
import {render, screen} from "@testing-library/react";

describe("<Footer/>", () => {
    it("render footer title", () => {
        render(<Footer/>)
        expect(screen.getByText("EasySocHacks")).toBeInTheDocument()
    });
})