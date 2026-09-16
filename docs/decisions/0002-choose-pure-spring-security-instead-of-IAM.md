# Choose pure "Spring Security" instead of IAM

## 1. Before

The old version used Keycloak as the authentication provider, running as a separate service on GCP.
The reason at that time: I read that Keycloak was "enterprise standard", "supported SSO/OAuth2/OIDC"
and there was a "Spring boot + Keycloak" tutorial, so i followed it. No analysis was done.

## 2. Reason

### Lack of understanding of the root of authentication.

Keycloak obscures the entire flow: SecurityFilterChain, AuthenticationManager, UserDetailsService,
SecurityContextHolder, session fixation, CSRF.
Users only see "redirect to Keycloak -> return with token". This is the root problem of the entire
rebuild project: using abstraction without understanding what it abstracts.

## 3. Solution

Use pure Spring Security, session-based/token-based, no external IAM.

## 4. After

Understand the entire auth flow from HTTP request → SecurityFilterChain → AuthenticationManager
→ UserDetailsService → SecurityContext → session.

### Answer interview questions:

- How does session work and where is it saved?

- What is CSRF, why does session stick but JWT doesn't?

- What is session fixation, what does migrateSession() do

- What is the difference between UserDetailsService and AuthenticationProvider?

- Why is constructor injection better than field injection in SecurityConfig

- User and business data are in the same DB → FK, transaction, simple backup.
