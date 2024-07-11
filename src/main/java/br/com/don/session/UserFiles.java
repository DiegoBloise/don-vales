package br.com.don.session;

import java.io.InputStream;
import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Data;

@Data
@Named
@SessionScoped
public class UserFiles implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean autoPrintVale = false;

    InputStream inputStream;

    public UserFiles() {

    }
}
