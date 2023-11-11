package com.mygolfclub.config.docs;

import com.mygolfclub.utils.reader.FileReader;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@OpenAPIDefinition
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {
        // Responses setup:
        final String resourcesPath = "src/main/resources/";
        final String responsePath = resourcesPath + "api/examples/response/";
        final String validResponsePath = responsePath + "ifValidRequest/";
        final String notValidResponsePath = responsePath + "ifNotValidRequest/";
        final String badRequestPhrase = "Bad Request";

        // First valid:
        // GET
        ApiResponse get = apiResponse(
                "if members found",
                FileReader.readToString(validResponsePath + "get/getResponse.json"),
                "Ok"
        );
        ApiResponse getById = apiResponse(
                "if member found by id",
                FileReader.readToString(validResponsePath + "get/by/id/getByIdResponse.json"),
                "Ok"
        );

        // POST
        ApiResponse post = apiResponse(
                "if member added",
                FileReader.readToString(validResponsePath + "post/postResponse.json"),
                "Created"
        );

        // PUT
        ApiResponse put = apiResponse(
                "if member updated",
                FileReader.readToString(validResponsePath + "put/putResponse.json"),
                "Created"
        );

        //DELETE
        ApiResponse delete = apiResponse(
                "any if authorized",
                HttpEntity.EMPTY,
                "No Content"
        );

        // Finally not valid:
        ApiResponse ifNotFoundById = apiResponse(
                "member not found by id",
                FileReader.readToString(notValidResponsePath + "by/id/notFoundByIdResponse.json"),
                "Not Found"
        );
        ApiResponse ifForbidden = apiResponse("if not authorized",
                FileReader.readToString(notValidResponsePath + "forbiddenResponse.json"),
                "Forbidden"
        );
        ApiResponse ifForbiddenById = apiResponse(
                "if not authorized",
                FileReader.readToString(notValidResponsePath + "by/id/forbiddenByIdResponse.json"),
                "Forbidden"
        );
        // SETUP MULTIPLE EXAMPLES OF RESPONSE
        // POST
        var mapOfNotValid =
                Map.of("if invalid request body",
                        FileReader.readToString(notValidResponsePath + "post/postBadRequestBodyResponse.json"),
                        "if http message is unreadable",
                        FileReader.readToString(notValidResponsePath + "post/postHTTPMessageNotReadableResponse.json")
                );
        ApiResponse postNotValid = apiResponse(mapOfNotValid, badRequestPhrase);
        // PUT
        mapOfNotValid =
                Map.of("if invalid request body",
                        FileReader.readToString(notValidResponsePath + "put/putBadRequestBodyResponse.json"),
                        "if http message is unreadable",
                        FileReader.readToString(notValidResponsePath + "put/putHTTPMessageNotReadableResponse.json")
                );
        ApiResponse putNotValid = apiResponse(mapOfNotValid, badRequestPhrase);

        return new OpenAPI()
                .info(new Info()
                        .title("MyGolfClub API DOCS")
                        .version("1.0")
                        .description("Official documentation for MyGolfClub API.")
                        .contact(new Contact().name("Lucas").url("https://github.com/lucasmalara"))
                ).components(new Components()
                        .addResponses("get", get)
                        .addResponses("getById", getById)
                        .addResponses("post", post)
                        .addResponses("putById", put)
                        .addResponses("delete", delete)
                        .addResponses("notFoundById", ifNotFoundById)
                        .addResponses("forbiddenById", ifForbiddenById)
                        .addResponses("forbidden", ifForbidden)
                        .addResponses("postNotValid", postNotValid)
                        .addResponses("putNotValid", putNotValid)
                );
    }

    private ApiResponse apiResponse(Map<String, String> bodies, String description) {
        return new ApiResponse()
                .content(new Content()
                        .addMediaType(APPLICATION_JSON_VALUE, addExamples(bodies))
                ).description(description);
    }

    private ApiResponse apiResponse(String exampleName, Object body, String description) {
        return new ApiResponse()
                .content(new Content()
                        .addMediaType(APPLICATION_JSON_VALUE,
                                new MediaType()
                                        .addExamples(exampleName,
                                                new Example().value(body)
                                        )
                        )
                ).description(description);
    }

    private MediaType addExamples(Map<String, String> bodies) {
        MediaType mediaType = new MediaType();
        bodies.forEach((exampleName, body) ->
                mediaType.addExamples(exampleName, new Example().value(body))
        );
        return mediaType;
    }
}
