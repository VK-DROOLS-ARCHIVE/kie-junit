package org.kie.junit;

import org.drools.core.io.impl.InputStreamResource;
import org.drools.core.util.IoUtils;
import org.junit.runners.model.InitializationError;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.KieContainer;

import java.io.IOException;

import static org.drools.compiler.kie.builder.impl.KieBuilderImpl.generatePomXml;
import static org.junit.Assert.assertTrue;

class KieCreationHelper {

    public KieCreationHelper(KieTestHelper kieTester) throws InitializationError {
        try {
            KieServices ks = KieServices.Factory.get();

            ReleaseId releaseId = ks.newReleaseId("org.kie", "kie-junit-sample", "1.0-SNAPSHOT");
            build(kieTester, ks, releaseId);
            KieContainer kieContainer = ks.newKieContainer(releaseId);
            kieTester.setKieBase(kieContainer.getKieBase());
            kieTester.setReleaseId(releaseId);
        } catch (IOException e) {
            throw new InitializationError(e);
        }
    }

    void build(KieTestHelper kieTester, KieServices ks, ReleaseId releaseId) throws IOException {

        KieModuleModel kproj = ks.newKieModuleModel();
        kproj.newKieBaseModel("KBase1").setDefault(true);
        KieFileSystem kfs = ks.newKieFileSystem();

        kfs.writeKModuleXML(kproj.toXML())
                .writePomXML( generatePomXml(releaseId) );

        int rCount=1;
        for (InputStreamResource inputStreamResource : kieTester.resourceList) {
            byte[] bytes = IoUtils.readBytesFromInputStream(inputStreamResource.getInputStream());
            kfs.write("src/main/resources/rules" + (rCount++)+"."+ inputStreamResource.getResourceType().getDefaultExtension(), bytes);
        }

        KieBuilder kieBuilder = ks.newKieBuilder(kfs);
        for ( Message message : kieBuilder.buildAll().getResults().getMessages()){
            System.err.println(message.getLine()+","+message.getColumn()+": "+message.getText());
        }
        assertTrue(kieBuilder.buildAll().getResults().getMessages().isEmpty());
    }
}
