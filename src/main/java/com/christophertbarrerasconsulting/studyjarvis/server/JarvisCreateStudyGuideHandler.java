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

public class JarvisCreateStudyGuideHandler implements Handler {
    public static Handler getInstance() {
        return HandlerDecorator.getInstance(new JarvisCreateStudyGuideHandler());
    }

    @OpenApi(
            summary = "Create Study Guide",
            description = "Returns study guide on all topics.",
            operationId = "createStudyGuide",
            path = "/secure/jarvis/create-study-guide",
            methods = {HttpMethod.POST},
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "Returned study guide",
                            content = @OpenApiContent(
                                    mimeType = "text/plain",
                                    example = "## UML - Unified Modeling Language Study Guide\n" +
                                            "\n" +
                                            "**Q: What is UML?**\n" +
                                            "\n" +
                                            "**A:** UML stands for Unified Modeling Language. It's a graphical language used to visualize, specify, construct, and document the artifacts of a software system. \n" +
                                            "\n" +
                                            "**Q: What is the purpose of UML?**\n" +
                                            "\n" +
                                            "**A:** UML helps in understanding and communicating complex system designs by providing a standard way to visualize:\n" +
                                            "    * System behavior\n" +
                                            "    * Object interactions\n" +
                                            "    * System architecture\n" +
                                            "    * Data structures\n" +
                                            "\n" +
                                            "**Q: What makes UML diagrams powerful?**\n" +
                                            "\n" +
                                            "**A:** UML diagrams offer various perspectives on a system, allowing for a comprehensive understanding from different viewpoints. This helps in:\n" +
                                            "    * Analyzing requirements\n" +
                                            "    * Designing solutions\n" +
                                            "    * Communicating ideas effectively among stakeholders \n" +
                                            "\n" +
                                            "**Q: How did UML come about?**\n" +
                                            "\n" +
                                            "**A:**  UML emerged in the mid-1990s to address the challenges of Object-Oriented (OO) design. Three leading OO researchers combined their respective modeling languages:\n" +
                                            "    * **Grady Booch** contributed **BOOCH**.\n" +
                                            "    * **Jim Rumbaugh** contributed **OML (Object Modeling Technique)**.\n" +
                                            "    * **Ivar Jacobsen** contributed **OOSE (Object-Oriented Software Engineering)**.\n" +
                                            "\n" +
                                            "**Q: What does \"Union of All Modeling Languages\" signify in the context of UML?**\n" +
                                            "\n" +
                                            "**A:** It highlights the collaborative effort behind UML's creation, where the best aspects of existing modeling languages (BOOCH, OML, OOSE) were integrated.\n" +
                                            "\n" +
                                            "Let me know if you'd like me to elaborate on a specific aspect of UML or have any other questions!"
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
            String studyGuide = jarvis.createStudyGuide();
            context.json(studyGuide);
        }
    }
}
