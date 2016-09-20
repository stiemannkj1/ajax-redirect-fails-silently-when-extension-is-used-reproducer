# [JAVASERVERFACES-4187](https://java.net/jira/browse/JAVASERVERFACES-4187) Reproducer

This project is a reproducer for
[JAVASERVERFACES-4187](https://java.net/jira/browse/JAVASERVERFACES-4187): *Ajax redirect fails silently when extension is used*

Ajax redirects fail silently when the `<extension>` element is used in the partial response:

```
<?xml version='1.0' encoding='UTF-8'?>
<partial-response id="j_id1">
<changes>
<extension parameterNamespace="j_id1"></extension>
</changes>
<redirect url="/ajax-redirect-fails-silently-when-extension-is-used-reproducer-1.0-SNAPSHOT/faces/page2.xhtml"></redirect>
</partial-response>
```

If the extension element is removed, redirects work correctly:

```
<?xml version='1.0' encoding='UTF-8'?>
<partial-response id="j_id1">
<redirect url="/ajax-redirect-fails-silently-when-extension-is-used-reproducer-1.0-SNAPSHOT/faces/page2.xhtml"></redirect>
</partial-response>
```

## Steps to reproduce:

1. Clone the reproducer project:

        git clone https://github.com/stiemannkj1/ajax-redirect-fails-silently-when-extension-is-used-reproducer &&
            cd ajax-redirect-fails-silently-when-extension-is-used-reproducer

2. Build the project with maven:

        mvn clean install

3. Deploy the project to tomcat:

        cp target/ajax-redirect-fails-silently-when-extension-is-used-reproducer*.war $TOMCAT_HOME/webapps/

4. Navigate to **`index.xhtml`** ([http://localhost:8080/ajax-redirect-fails-silently-when-extension-is-used-reproducer-1.0-SNAPSHOT/faces/index.xhtml](http://localhost:8080/ajax-redirect-fails-silently-when-extension-is-used-reproducer-1.0-SNAPSHOT/faces/index.xhtml)).

5. Click on the *page2* button.

If the bug still exists, an ajax POST will occur with a redirect, but the browser will stay on **`index.html`** instead of navigating to **`page2.xhtml`**.

## Steps to test the fix:

1. Fetch and checkout my fixed Mojarra branch:

        cd mojarra~git/ &&
            git remote add stiemannkj1 https://github.com/stiemannkj1/mojarra.git &&
            git fetch stiemannkj1 ajax-redirect-fails-silently-when-extension-is-used-fix &&
            git checkout ajax-redirect-fails-silently-when-extension-is-used-fix

2. Build Mojarra according to the instructions [here](http://stackoverflow.com/questions/10964606/how-to-build-mojarra-from-source).

3. Build the project with the newly compiled Mojarra version:

        mvn clean install -Dmojarra.version=2.2.14-SNAPSHOT

4. Deploy the project to tomcat:

        cp target/ajax-redirect-fails-silently-when-extension-is-used-reproducer*.war $TOMCAT_HOME/webapps/

5. Navigate to **`index.xhtml`** ([http://localhost:8080/ajax-redirect-fails-silently-when-extension-is-used-reproducer-1.0-SNAPSHOT/faces/index.xhtml](http://localhost:8080/ajax-redirect-fails-silently-when-extension-is-used-reproducer-1.0-SNAPSHOT/faces/index.xhtml)).

6. Click on the *page2* button.

If the bug is fixed, the browser will show "Page 2" since it navigated to **`page2.xhtml`**.
