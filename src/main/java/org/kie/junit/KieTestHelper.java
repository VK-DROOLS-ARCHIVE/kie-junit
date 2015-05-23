package org.kie.junit;

import org.drools.core.io.impl.InputStreamResource;
import org.junit.runners.model.InitializationError;
import org.kie.api.KieBase;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.ResourceType;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class KieTestHelper {

    protected boolean initialized = false;
    protected KieBase kieBase;
    protected List<InputStreamResource> resourceList;
    private ReleaseId releaseId;

    public KieTestHelper() {
        resourceList = new ArrayList<InputStreamResource>();
    }

    public KieBase getKieBase(){
        return kieBase;
    }

    protected void setKieBase(KieBase kieBase) {
        this.kieBase = kieBase;
    }

    public static StatelessSessionTestHelper newStatelessSession(){
        return new StatelessSessionTestHelper();
    }

    public static StatefulSessionTestHelper newStatefulSession(){
        return new StatefulSessionTestHelper();
    }

    public KieTestHelper addKieResource(ResourceType resourceType, InputStream inputStream) throws InitializationError {
        if (initialized){
            throw new InitializationError("Cannot addKieResource, KieBase already built!");
        }
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        inputStreamResource.setResourceType(resourceType);
        resourceList.add(inputStreamResource);
        return this;
    }

    public KieTestHelper addDRL(InputStream inputStream) throws InitializationError {
        return addKieResource(ResourceType.DRL, inputStream);
    }

    public KieTestHelper addDTable(InputStream inputStream) throws InitializationError {
        return addKieResource(ResourceType.DTABLE, inputStream);
    }

    public KieTestHelper addDSL(InputStream inputStream) throws InitializationError {
        return addKieResource(ResourceType.DSL, inputStream);
    }

    public KieTestHelper addSCard(InputStream inputStream) throws InitializationError {
        return addKieResource(ResourceType.SCARD, inputStream);
    }

    public KieTestHelper addBPMN2(InputStream inputStream) throws InitializationError {
        return addKieResource(ResourceType.BPMN2, inputStream);
    }

    public KieTestHelper build() throws InitializationError {
        if (initialized){
            throw new InitializationError("KieBase already built! build() cannot be invoked twice!");
        }
        new KieCreationHelper(this);
        initialized = true;
        resourceList.clear();
        return this;
    }

    public void setReleaseId(ReleaseId releaseId) {
        this.releaseId = releaseId;
    }

    public ReleaseId getReleaseId() {
        return releaseId;
    }

    public abstract Map<String,Integer> execute(Object object) throws InitializationError;
}
