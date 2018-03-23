https://appdev.openshift.io/docs/spring-boot-runtime.html#mission-circuit-breaker-spring-boot-tomcat

# Istio-specific instructions

## Add this project's `default` service account to `privileged` security context: 

```bash
oc adm policy add-scc-to-user privileged -z default -n $(oc project -q)
```
