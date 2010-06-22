package org.jbehave.core.configuration.spring;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import java.util.List;

import org.jbehave.core.annotations.WithConfiguration;
import org.jbehave.core.annotations.spring.UsingSpring;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.failures.SilentlyAbsorbingFailure;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.Steps;
import org.jbehave.core.steps.spring.SpringStepsFactoryBehaviour.FooSteps;
import org.jbehave.core.steps.spring.SpringStepsFactoryBehaviour.FooStepsWithDependency;
import org.junit.Test;

public class SpringAnnotationBuilderBehaviour {

    private SpringAnnotationBuilder builder = new SpringAnnotationBuilder();

    @Test
    public void shouldCreateConfigurationFromAnnotation(){
        Configuration configuration = builder.buildConfiguration(new Annotated());
        assertThat(configuration.failureStrategy(), instanceOf(SilentlyAbsorbingFailure.class));
    }

    @Test
    public void shouldCreateCandidateStepsFromAnnotation(){
        assertThatStepsInstancesAre(builder.buildCandidateSteps(new Annotated()), FooSteps.class, FooStepsWithDependency.class);
    }
    
    @Test
    public void shouldCreateEmptyCandidateStepsListIfAnnotationOrAnnotatedValuesNotPresent(){
        assertThatStepsInstancesAre(builder.buildCandidateSteps(new NotAnnotated()));
        assertThatStepsInstancesAre(builder.buildCandidateSteps(new AnnotatedWithoutSteps()));
    }

    private void assertThatStepsInstancesAre(List<CandidateSteps> candidateSteps, Class<?>... stepsClasses){
        assertThat(candidateSteps.size(), equalTo(stepsClasses.length));
        for (int i = 0; i < stepsClasses.length; i++ ){
            assertThat(((Steps)candidateSteps.get(i)).instance(), instanceOf(stepsClasses[i]));
        }        
    }
    

    @WithConfiguration()
    @UsingSpring(locations={"org/jbehave/core/configuration/spring/configuration.xml", "org/jbehave/core/steps/spring/steps.xml", "org/jbehave/core/steps/spring/steps-with-dependency.xml"})
    private static class Annotated {
        
    }

    @UsingSpring()
    private static class AnnotatedWithoutSteps {
        
    }

    private static class NotAnnotated {
        
    }
    
}
