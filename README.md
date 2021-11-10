### This is a online chat messages based on IFMO-Software-Testing course's homeworks.

## Run project
To run this project you can do it either with [docker](https://www.docker.com/) or manually:
<details>
<summary>Docker</summary>

From [root](/):
* Execute ```docker-compose build``` (you have to do it only once)
* Execute ```docker-compose up``` for run the project after build 

</details>

<details>
<summary>Manually</summary>

* Run mongodb on port 27017. For example by starting docker image by executing ```docker run -p 27017:27017 mongo```
* Goto [api](/api) and start spring server by running [Application.kt](/api/src/main/kotlin/easy/soc/hacks/spring/Application.kt) file
* Goto [client](/client) and start react js server using command ```npm start``` (execute ```npm install``` for the first run)

</details>

Now you can use project by connecting to the following url:
* ```http://localhost:3000/``` for interactions with client
* ```http://localhost:8080/``` for interactions with api

## Test project
Project contains tests for:
* [client](/client)
  * **unit** tests runs by executing command ```npm test src/test/unit/``` (must runs from [client](/client) directory)
  * **e2e** tests runs by executing command ```npm test src/test/e2e/``` (must runs from [client](/client) directory)