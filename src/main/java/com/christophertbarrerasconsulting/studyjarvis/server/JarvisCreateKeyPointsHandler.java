package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiResponse;
import org.jetbrains.annotations.NotNull;

public class JarvisCreateKeyPointsHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new JarvisCreateKeyPointsHandler());
    }

    @OpenApi(
            summary = "Create Key Points",
            description = "Returns key points on all topics.",
            operationId = "createKeyPoints",
            path = "/secure/jarvis/create-key-points",
            methods = {HttpMethod.POST},
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "Returned key points",
                            content = @OpenApiContent(
                                    mimeType = "text/plain",
                                    example = "## Key Points:\n" +
                                            "\n" +
                                            "**UML (Unified Modeling Language):**\n" +
                                            "\n" +
                                            "* **Purpose:** A graphical language used to visualize, specify, construct, and document the artifacts of a software system.\n" +
                                            "* **Nature:** Consists of various diagram types, each providing a different perspective on the system.\n" +
                                            "* **Origin:** Created to unify the various object-oriented modeling approaches in the 1990s.\n" +
                                            "\n" +
                                            "**Object-Oriented Design (OOD) and UML:**\n" +
                                            "\n" +
                                            "* **Motivation:** UML aims to promote and facilitate object-oriented design principles.\n" +
                                            "* **Key Contributors:** Grady Booch (BOOCH), Jim Rumbaugh (OML), and Ivar Jacobson (OOSE) merged their respective modeling languages to form UML.\n" +
                                            "* **Historical Context:**  The unification of these languages occurred in the mid-1990s, reflecting a growing need for standardization in the field of object-oriented software development."
                            )
                    ),
                    @OpenApiResponse(status = "401", description = "Unauthorized"),
                    @OpenApiResponse(status = "500", description = "Internal server error")
            }
    )

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.attribute("username");
        User user = UserReader.getUser(username);
        int userId = user.getUserId();

        try (Jarvis jarvis = Jarvis.getInstance(userId)) {
            String keyPoints = jarvis.createKeyPoints();
            context.json(keyPoints);
        }
    }
}
