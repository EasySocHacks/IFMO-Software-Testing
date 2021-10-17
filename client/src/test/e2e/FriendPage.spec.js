import {Browser, chromium, Page} from "playwright";

let browser: Browser;
beforeAll(async () => {
    browser = await chromium.launch();
});
afterAll(async () => {
    await browser.close();
});

let page: Page;
beforeEach(async () => {
    page = await browser.newPage();
});
afterEach(async () => {
    await page.close();
});

describe("FriendsPage tests", () => {
    it("FriendsPage contains Friends title", async () => {
        await page.goto("http://localhost:3000/friends");
        const text = await page.locator("h3").innerText();

        await expect(text).toEqual("Friends");
    });

    it("Header FriendsPage tab refers to /friends page", async () => {
        await page.goto("http://localhost:3000/");
        await page.locator(".friendsTabLink").evaluate(e => e.click())

        await expect(page.url()).toEqual("http://localhost:3000/friends")
    })

    it("Footer FriendsPage tab refers to github page", async () => {
        await page.goto("http://localhost:3000/");
        await page.locator(".footer").locator("a").evaluate(e => e.click())

        await expect(page.url()).toEqual("https://github.com/EasySocHacks")
    })
})