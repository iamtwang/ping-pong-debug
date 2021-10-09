## HttpGet With Request Body & RestTemplate
- Third party REST API offered HTTP GET endpoint and expect Request Body.
- In Spring Boot, RestTemplate do not send request boy for GET by default.
- Use Apache HttpClient to instead of RestTemplate default.

### The work
- create a Rest Endpoint /data/info to mock the 3rd party API
    - it's HTTP Get and require request body
    - trigger this API from /hello/{name}
    - branch main shows how to hit it correctly with Http Client
    - branch dev-issue shows the issue and explain why RestTemple do not send it.

