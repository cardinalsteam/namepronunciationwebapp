package com.namepronunciation.service;

import com.microsoft.cognitiveservices.speech.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
@Slf4j
public class AzureTextToSpeechHelper {

    //Using Azure text to speech service sdk.
    public void callAzureToTransformTextToSpeech(String text){
        //create a azure speech resource/speech services, and get the key from there.
        /*String speechSubscriptionKey = "2f6f7536157744cea209f4398d39cf12";
        String serviceRegion = "westus2";*/

        String speechSubscriptionKey = "102cb5eceb6d42e6952535c67884693b";

        String serviceRegion = "eastus";
        try {
           // SpeechConfig config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
SpeechConfig config = SpeechConfig.fromEndpoint(new URI("https://eastus.api.cognitive.microsoft.com/sts/v1.0/issuetoken"),speechSubscriptionKey);
            config.setSpeechSynthesisVoiceName("en-US-AriaNeural");
            SpeechSynthesizer synth = new SpeechSynthesizer(config);

            assert(config != null);
            assert(synth != null);

            int exitCode = 1;

            Future<SpeechSynthesisResult> task = synth.SpeakTextAsync(text);
            assert(task != null);

            SpeechSynthesisResult result = task.get();
            assert(result != null);

            if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
                System.out.println("Speech synthesized to speaker for text [" + text + "]");
                exitCode = 0;
            }
            else if (result.getReason() == ResultReason.Canceled) {
                SpeechSynthesisCancellationDetails cancellation = SpeechSynthesisCancellationDetails.fromResult(result);
                System.out.println("CANCELED: Reason=" + cancellation.getReason());

                if (cancellation.getReason() == CancellationReason.Error) {
                    System.out.println("CANCELED: ErrorCode=" + cancellation.getErrorCode());
                    System.out.println("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
                    System.out.println("CANCELED: Did you set the speech resource key and region values?");
                }
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
            log.debug("InterruptedException: "+e.getCause());
            System.out.println("InterruptedException exception: " + e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            log.debug("ExecutionException: "+e.getStackTrace());
            System.out.println("ExecutionException exception: " + e.getMessage());
        }catch (Exception e){
            log.debug("Exception occured: "+e.getStackTrace());
        }


    }
}
