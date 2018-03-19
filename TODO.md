# Instructions to configure Istio for automated injection

## Prerequisites

- Minishift with OpenShift 3.9.0
- Istio 0.6.0 installed

## Configure Kubernetes Web Hook (feature only available with kube 1.9)

See [here](https://istio.io/docs/setup/kubernetes/sidecar-injection.html) - `installing the web hook`

- [Trusting](https://kubernetes.io/docs/tasks/tls/managing-tls-in-a-cluster/) TLS for your cluster
- Create certificate for the web hook

```bash
cd ~/.istio/istio-0.6.0
./install/kubernetes/webhook-create-signed-cert.sh \
    --service istio-sidecar-injector \
    --namespace istio-system \
    --secret sidecar-injector-certs
creating certs in tmpdir /var/folders/56/dtp67r4n1hv79q2hrh_dbwcc0000gn/T/tmp.OMMuwlf2
Generating RSA private key, 2048 bit long modulus
....................................+++
.........................................................................+++
e is 65537 (0x10001)
certificatesigningrequest "istio-sidecar-injector.istio-system" created
NAME                                  AGE       REQUESTOR   CONDITION
istio-sidecar-injector.istio-system   0s        admin       Pending
certificatesigningrequest "istio-sidecar-injector.istio-system" approved
ERROR: After approving csr istio-sidecar-injector.istio-system, the signed certificate did not appear on the resource. Giving up after 10 attempts.
See https://istio.io/docs/setup/kubernetes/sidecar-injection.html for more details on troubleshooting.
```

- Install the sidecar injection configmap.
```bash
oc apply -f install/kubernetes/istio-sidecar-injector-configmap-release.yaml
configmap "istio-inject" created
```

- Set the `caBundle` in the webhook install YAML that the Kubernetes api-server uses to invoke the webhook.

```bash
cat install/kubernetes/istio-sidecar-injector.yaml | \
       ./install/kubernetes/webhook-patch-ca-bundle.sh > \
       install/kubernetes/istio-sidecar-injector-with-ca-bundle.yaml

```  
- Install the sidecar injector webhook.
  
```bash
oc apply -f install/kubernetes/istio-sidecar-injector-with-ca-bundle.yaml
```  
  
-  The sidecar injector webhook should now be running.
```bash
oc -n istio-system get deployment -listio=sidecar-injector
```

## NameSpace selector

NamespaceSelector decides whether to run the webhook on an object based on whether the namespace for that object matches
the selector (see https://kubernetes.io/docs/concepts/overview/working-with-objects/labels/#label-selectors).
The default webhook configuration uses `istio-injection=enabled`.

View namespaces showing `istio-injection` label and verify the default namespace is not labeled.

```bash
oc get namespace -L istio-injection
```


    