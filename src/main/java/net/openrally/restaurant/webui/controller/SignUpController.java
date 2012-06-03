package net.openrally.restaurant.webui.controller;

import java.io.IOException;

import net.openrally.restaurant.response.body.CompanyResponseBody;
import net.openrally.restaurant.webui.manager.CompanyManager;
import net.openrally.restaurant.webui.util.ReCaptcha;

import org.apache.http.client.ClientProtocolException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

public class SignUpController extends SelectorComposer<Div> {

	private static final long serialVersionUID = -1187323119263024517L;

	@Wire
	private Button contentButtonCreateCompany;

	@Wire
	private Label contentLabelCompanyIdValue;

	@Wire
	private Label contentLabelUsernameValue;

	@Wire
	private Label contentLabelPasswordValue;
	
	@Wire
	private Vbox contentVboxReCaptcha;
	
	@Wire
	private Image contentImageReCaptcha;

	@Wire
	private Textbox contentTextboxCaptchaResponse;

	@Wire
	private Groupbox groupBoxWelcome;

	private ReCaptcha reCaptcha;

	@Override
	public void doAfterCompose(Div comp) throws Exception {
		super.doAfterCompose(comp);

		reloadReCaptcha();
	}
	
	public void reloadReCaptcha() throws ClientProtocolException, IOException{
		reCaptcha = new ReCaptcha();

		contentImageReCaptcha.setSrc(reCaptcha.getImagePath());
	}

	@Listen("onClick=#contentButtonCreateCompany")
	public void contentLabelCreateCompany() throws ClientProtocolException,
			IOException {
		validate();

		contentButtonCreateCompany.setDisabled(true);
		contentButtonCreateCompany.setVisible(false);

		CompanyResponseBody newCompany = CompanyManager.createNewCompany();

		contentLabelCompanyIdValue.setValue(String.valueOf(newCompany
				.getCompanyId()));
		contentLabelUsernameValue.setValue(String.valueOf(newCompany
				.getUsername()));
		contentLabelPasswordValue.setValue(String.valueOf(newCompany
				.getPassword()));

		groupBoxWelcome.setVisible(true);
		contentVboxReCaptcha.setVisible(false);

	}

	private void validate() throws WrongValueException, ClientProtocolException, IOException {
		String remoteAddr = Executions.getCurrent().getRemoteAddr();

		boolean captchaCheckResult = reCaptcha.verifyResponse(remoteAddr,
				contentTextboxCaptchaResponse.getValue());

		if (!captchaCheckResult) {
			reloadReCaptcha();
			throw new WrongValueException(contentTextboxCaptchaResponse, "ReCaptcha doesn't match. Please try again");
		}

	}
}
