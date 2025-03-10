package com.christophertbarrerasconsulting.studyjarvis.server;

import com.christophertbarrerasconsulting.studyjarvis.Jarvis;
import com.christophertbarrerasconsulting.studyjarvis.user.User;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.openapi.*;
import org.jetbrains.annotations.NotNull;

public class JarvisCreateNotesHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new JarvisCreateNotesHandler());
    }

    @OpenApi(
            summary = "Create Notes",
            description = "Returns comprehensive notes on all topics.",
            operationId = "createNote",
            path = "/secure/jarvis/create-notes",
            methods = {HttpMethod.POST},
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "Returned notes",
                            content = @OpenApiContent(
                                    mimeType = "text/plain",
                                    example = "## UML - Unified Modeling Language \n" +
                                            "\n" +
                                            "This note describes the Unified Modeling Language (UML), its purpose, and its origins. \n" +
                                            "\n" +
                                            "**What is UML?**\n" +
                                            "\n" +
                                            "UML is a graphical language used for visualizing, specifying, constructing, and documenting software systems. It is primarily used for object-oriented systems but can be applied to other paradigms.  Think of it as a blueprint for software development.\n" +
                                            "\n" +
                                            "**Key Characteristics:**\n" +
                                            "\n" +
                                            "* **Graphical:**  UML uses diagrams to represent system elements and their relationships, making it easier to understand complex systems.\n" +
                                            "* **Multi-viewpoint:**  UML offers various diagram types to capture different aspects of a system, from different perspectives.\n" +
                                            "* **System-focused:**  UML models focus on understanding and designing the system as a whole, not just its individual components.\n" +
                                            "\n" +
                                            "**Purpose of UML:**\n" +
                                            "\n" +
                                            "UML helps software developers:\n" +
                                            "\n" +
                                            "* **Communicate design ideas:**  Diagrams provide a common visual language for stakeholders to discuss and refine system requirements and design choices.\n" +
                                            "* **Model system behavior:** UML diagrams can show how a system interacts with its environment, users, and other systems.\n" +
                                            "* **Document system architecture:**  UML serves as a blueprint for implementation and future maintenance of the software.\n" +
                                            "\n" +
                                            "**Origins of UML:**\n" +
                                            "\n" +
                                            "UML emerged in the mid-1990s from the unification of three prominent object-oriented modeling languages:\n" +
                                            "\n" +
                                            "* **BOOCH:** Created by Grady Booch, it was known for its detailed object modeling capabilities.\n" +
                                            "* **OML (Object Modeling Technique):** Developed by Jim Rumbaugh, it focused on capturing the structure and behavior of objects.\n" +
                                            "* **OOSE (Object-Oriented Software Engineering):** Introduced by Ivar Jacobson, it emphasized use cases and the interaction between objects.\n" +
                                            "\n" +
                                            "The creators of these languages collaborated to develop UML as a standard modeling language for object-oriented software development, aiming to simplify the modeling process and improve communication among developers.\n" +
                                            "\n" +
                                            "**In summary,** UML provides a powerful toolset for visualizing and designing software systems, particularly those following the object-oriented paradigm. By combining the strengths of earlier modeling approaches, UML has become a widely adopted standard in the software development industry."
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
            String notes = jarvis.createComprehensiveNotes();
            context.json(notes);
        }
    }
}
