package ar.edu.itba.paw.webapp.dto;

import javax.xml.bind.annotation.XmlElement;


public class ProfilePictureDto {

  @XmlElement(name = "content", required = true)  //TODO enforce, built with null if not passed, even when required
  private String base64Data;

  public ProfilePictureDto() {}

  public ProfilePictureDto(String base64Data) {
    this.base64Data = base64Data;
  }

  public String getBase64Data() {
    return base64Data;
  }

  public void setBase64Data(String base64Data) {
    this.base64Data = base64Data;
  }
}
