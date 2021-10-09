## HttpGet With Request Body & RestTemplate
- Can Http Get has Request Body
  - Yes
- Does RestTemplate Default setting support HttpGet with Request body?
  - No
- Third party REST API offered HTTP GET endpoint and expect Request Body.
- In Spring Boot, RestTemplate do not send request boy for GET by default.
- Use Apache HttpClient for HttpGet with request body.

### The work
- create a Rest Endpoint /data/info to mock the 3rd party API
    - it's HTTP Get and require request body
    - trigger this API from /hello/{name}
    - branch main shows how to hit it correctly with Http Client
    - branch dev-issue shows the issue and explain why RestTemple do not send it.

