package ar.edu.itba.paw.webapp.dto;


import ar.edu.itba.paw.webapp.api_errors.*;
import ar.edu.itba.paw.webapp.model.ValidationException;

import javax.xml.bind.annotation.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This DTO is used to send errors to the client using the API.
 * It includes (as subclasses) all kind of errors the API can generate.
 * <p>
 * Created by Juan Marcos Bellini on 14/12/16.
 */
@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorDto {

    @XmlElement(required = true)
    private String errorKind;


    public ErrorDto(ApiError error) {
        this.errorKind = error.getErrorKind();
    }


    public ErrorDto() {
        // Default constructor
    }


    public String getErrorKind() {
        return errorKind;
    }

    @XmlType(name = "")
    public static class ClientSideErrorDto extends ErrorDto {

        @XmlElement(required = true)
        private String message;

        @XmlElement(required = true)
        private int groupCode;

        public ClientSideErrorDto() {
            // Default constructor
        }

        public ClientSideErrorDto(ClientSideError error) {
            super(error);
            this.message = error.getMessage();
            this.groupCode = error.getGroupCode();
        }


        public String getMessage() {
            return message;
        }

        public int getGroupCode() {
            return groupCode;
        }

        @XmlType(name = "")
        public static class InvalidParameterValueErrorDto extends ClientSideErrorDto {

            @XmlElement(required = true)
            private List<String> conflictingParameters;

            public InvalidParameterValueErrorDto() {
                // Default constructor
            }

            public InvalidParameterValueErrorDto(InvalidParameterValueError error) {
                super(error);
                this.conflictingParameters = new LinkedList<>(error.getConflictingParameters());
            }

            public List<String> getConflictingParameters() {
                return conflictingParameters;
            }
        }


        @XmlType(name = "")
        public static class IllegalParameterValueErrorDto extends ClientSideErrorDto {

            @XmlElement(required = true)
            private List<String> conflictingParameters;

            public IllegalParameterValueErrorDto() {
                // Default constructor
            }

            public IllegalParameterValueErrorDto(IllegalParameterValueError error) {
                super(error);
                this.conflictingParameters = new LinkedList<>(error.getConflictingParameters());
            }

            public List<String> getConflictingParameters() {
                return conflictingParameters;
            }
        }


        @XmlType(name = "")
        public static class RepresentationErrorDto extends ClientSideErrorDto {


            public RepresentationErrorDto() {
                // Default constructor
            }

            public RepresentationErrorDto(RepresentationError error) {
                super(error);
            }
        }

        @XmlType(name = "")
        public static class EntityNotPresentErrorDto extends ClientSideErrorDto {

            @XmlElement(required = true)
            private List<String> conflictingFields;

            public EntityNotPresentErrorDto() {
                // Default constructor
            }

            public EntityNotPresentErrorDto(EntityNotPresentError error) {
                super(error);
                this.conflictingFields = new LinkedList<>(error.getConflictingFields());
            }

            public List<String> getConflictingFields() {
                return conflictingFields;
            }
        }

        @XmlType(name = "")
        public static class ValidationErrorDto extends ClientSideErrorDto {

            @XmlElement(required = true)
            private List<SpecificValidationErrorDto> errors;

            public ValidationErrorDto() {
                // Default constructor
            }

            public ValidationErrorDto(ValidationError error) {
                super(error);
                errors = error.getErrors().stream().map(SpecificValidationErrorDto::new).collect(Collectors.toList());
            }

            public List<SpecificValidationErrorDto> getErrors() {
                return errors;
            }

            @XmlType(name = "")
            public static class SpecificValidationErrorDto {

                @XmlElement(required = true)
                private String errorCode;

                @XmlElement(required = true)
                private String conflictingField;

                @XmlElement
                private String message;

                public SpecificValidationErrorDto() {
                    // Default constructor
                }

                public SpecificValidationErrorDto(ValidationException.ValueError error) {
                    this.errorCode = error.getErrorCode().name();
                    this.conflictingField = error.getFieldName();
                    ;
                    this.message = error.getErrorMessage();
                }

                public String getErrorCode() {
                    return errorCode;
                }

                public String getConflictingField() {
                    return conflictingField;
                }

                public String getMessage() {
                    return message;
                }
            }
        }


    }
}
