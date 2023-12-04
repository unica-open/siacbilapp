/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.stampe;

import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.GenericAllegatoAttoModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiAllegatoAttoChecklist;

public class InviaAllegatoAttoChecklistModel extends GenericAllegatoAttoModel {

	private static final long serialVersionUID = 1172440679643623262L;
		
	private Integer uidAllegatoAtto;
	private String descrAllegatoAtto;
	
	public InviaAllegatoAttoChecklistModel() {
		super();
		setTitolo("Checklist Allegato Atto");
	}
	
	public Integer getUidAllegatoAtto() {
		return uidAllegatoAtto;
	}
	public void setUidAllegatoAtto(Integer uidAllegatoAtto) {
		this.uidAllegatoAtto = uidAllegatoAtto;
	}

	public LeggiAllegatoAttoChecklist buildLeggiAllegatoAttoChecklist() {
		return creaRequest(LeggiAllegatoAttoChecklist.class);
	}

	public String getDescrAllegatoAtto() {
		return descrAllegatoAtto;
	}

	public void setDescrAllegatoAtto(String descrAllegatoAtto) {
		this.descrAllegatoAtto = descrAllegatoAtto;
	}
}
