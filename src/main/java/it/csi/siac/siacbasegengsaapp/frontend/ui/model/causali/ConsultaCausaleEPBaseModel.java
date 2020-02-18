/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioCausale;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassificatoreEP;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe di model per la consultazione della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/04/2015
 *
 */
public abstract class ConsultaCausaleEPBaseModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6773153203342788633L;
	
	private CausaleEP causaleEP;
	private TipoEvento tipoEvento;
	
	private String labelClassificatoreEP1;
	private String labelClassificatoreEP2;
	private String labelClassificatoreEP3;
	private String labelClassificatoreEP4;
	private String labelClassificatoreEP5;
	private String labelClassificatoreEP6;
	
	private ClassificatoreEP classificatoreEP1;
	private ClassificatoreEP classificatoreEP2;
	private ClassificatoreEP classificatoreEP3;
	private ClassificatoreEP classificatoreEP4;
	private ClassificatoreEP classificatoreEP5;
	private ClassificatoreEP classificatoreEP6;
	
	/**
	 * Ottiene l'ambito corrispondente: pu&oacute; essere AMBITO_FIN o AMBITO_GSA.
	 * 
	 * @return l'ambito
	 */
	public abstract Ambito getAmbito();
	
	/**
	 * @return the ambitoFIN
	 */
	public Ambito getAmbitoFIN() {
		return Ambito.AMBITO_FIN;
	}
	/**
	 * @return the causaleEP
	 */
	public CausaleEP getCausaleEP() {
		return causaleEP;
	}

	/**
	 * @param causaleEP the causaleEP to set
	 */
	public void setCausaleEP(CausaleEP causaleEP) {
		this.causaleEP = causaleEP;
	}

	/**
	 * @return the tipoEvento
	 */
	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}
	
	/**
	 * @param tipoEvento the tipoEvento to set
	 */
	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	
	/**
	 * @return the labelClassificatoreEP1
	 */
	public String getLabelClassificatoreEP1() {
		return labelClassificatoreEP1;
	}

	/**
	 * @param labelClassificatoreEP1 the labelClassificatoreEP1 to set
	 */
	public void setLabelClassificatoreEP1(String labelClassificatoreEP1) {
		this.labelClassificatoreEP1 = labelClassificatoreEP1;
	}

	/**
	 * @return the labelClassificatoreEP2
	 */
	public String getLabelClassificatoreEP2() {
		return labelClassificatoreEP2;
	}

	/**
	 * @param labelClassificatoreEP2 the labelClassificatoreEP2 to set
	 */
	public void setLabelClassificatoreEP2(String labelClassificatoreEP2) {
		this.labelClassificatoreEP2 = labelClassificatoreEP2;
	}

	/**
	 * @return the labelClassificatoreEP3
	 */
	public String getLabelClassificatoreEP3() {
		return labelClassificatoreEP3;
	}

	/**
	 * @param labelClassificatoreEP3 the labelClassificatoreEP3 to set
	 */
	public void setLabelClassificatoreEP3(String labelClassificatoreEP3) {
		this.labelClassificatoreEP3 = labelClassificatoreEP3;
	}

	/**
	 * @return the labelClassificatoreEP4
	 */
	public String getLabelClassificatoreEP4() {
		return labelClassificatoreEP4;
	}

	/**
	 * @param labelClassificatoreEP4 the labelClassificatoreEP4 to set
	 */
	public void setLabelClassificatoreEP4(String labelClassificatoreEP4) {
		this.labelClassificatoreEP4 = labelClassificatoreEP4;
	}

	/**
	 * @return the labelClassificatoreEP5
	 */
	public String getLabelClassificatoreEP5() {
		return labelClassificatoreEP5;
	}

	/**
	 * @param labelClassificatoreEP5 the labelClassificatoreEP5 to set
	 */
	public void setLabelClassificatoreEP5(String labelClassificatoreEP5) {
		this.labelClassificatoreEP5 = labelClassificatoreEP5;
	}

	/**
	 * @return the labelClassificatoreEP6
	 */
	public String getLabelClassificatoreEP6() {
		return labelClassificatoreEP6;
	}

	/**
	 * @param labelClassificatoreEP6 the labelClassificatoreEP6 to set
	 */
	public void setLabelClassificatoreEP6(String labelClassificatoreEP6) {
		this.labelClassificatoreEP6 = labelClassificatoreEP6;
	}

	/**
	 * @return the classificatoreEP1
	 */
	public ClassificatoreEP getClassificatoreEP1() {
		return classificatoreEP1;
	}

	/**
	 * @param classificatoreEP1 the classificatoreEP1 to set
	 */
	public void setClassificatoreEP1(ClassificatoreEP classificatoreEP1) {
		this.classificatoreEP1 = classificatoreEP1;
	}

	/**
	 * @return the classificatoreEP2
	 */
	public ClassificatoreEP getClassificatoreEP2() {
		return classificatoreEP2;
	}

	/**
	 * @param classificatoreEP2 the classificatoreEP2 to set
	 */
	public void setClassificatoreEP2(ClassificatoreEP classificatoreEP2) {
		this.classificatoreEP2 = classificatoreEP2;
	}

	/**
	 * @return the classificatoreEP3
	 */
	public ClassificatoreEP getClassificatoreEP3() {
		return classificatoreEP3;
	}

	/**
	 * @param classificatoreEP3 the classificatoreEP3 to set
	 */
	public void setClassificatoreEP3(ClassificatoreEP classificatoreEP3) {
		this.classificatoreEP3 = classificatoreEP3;
	}

	/**
	 * @return the classificatoreEP4
	 */
	public ClassificatoreEP getClassificatoreEP4() {
		return classificatoreEP4;
	}

	/**
	 * @param classificatoreEP4 the classificatoreEP4 to set
	 */
	public void setClassificatoreEP4(ClassificatoreEP classificatoreEP4) {
		this.classificatoreEP4 = classificatoreEP4;
	}

	/**
	 * @return the classificatoreEP5
	 */
	public ClassificatoreEP getClassificatoreEP5() {
		return classificatoreEP5;
	}

	/**
	 * @param classificatoreEP5 the classificatoreEP5 to set
	 */
	public void setClassificatoreEP5(ClassificatoreEP classificatoreEP5) {
		this.classificatoreEP5 = classificatoreEP5;
	}

	/**
	 * @return the classificatoreEP6
	 */
	public ClassificatoreEP getClassificatoreEP6() {
		return classificatoreEP6;
	}

	/**
	 * @param classificatoreEP6 the classificatoreEP6 to set
	 */
	public void setClassificatoreEP6(ClassificatoreEP classificatoreEP6) {
		this.classificatoreEP6 = classificatoreEP6;
	}

	/**
	 * @return the denominazioneCausale
	 */
	public String getDenominazioneCausale() {
		StringBuilder sb = new StringBuilder();
		if(getCausaleEP() != null) {
			sb.append("Causale ")
				.append(getCausaleEP().getCodice())
				.append(" - ")
				.append(getCausaleEP().getDescrizione());
		}
		return sb.toString();
	}

	/**
	 * @return the tipoCausaleDiRaccordo
	 */
	public boolean isTipoCausaleDiRaccordo() {
		return getCausaleEP() != null && TipoCausale.Integrata.equals(getCausaleEP().getTipoCausale());
	}
	
	/**
	 * @return the numeroClassificatoriEP
	 */
	public int getNumeroClassificatoriEP() {
		return 6;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioCausale}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioCausale creaRequestRicercaDettaglioCausale() {
		RicercaDettaglioCausale request = creaRequest(RicercaDettaglioCausale.class);
		
		request.setCausaleEP(getCausaleEP());
		request.setBilancio(getBilancio());
		
		return request;
	}

}
