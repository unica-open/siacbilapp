/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.tipobenecespite;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;

/**
 * The Class InserisciTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class ConsultaTipoBeneModel extends GenericTipoBeneModel {

	/** Per la serializzazione*/
	private static final long serialVersionUID = 1L;
	
	private String categoriaCespite;
	private String contoPatrimonialeTipoBene;
	private String eventoAmmortamentoTipoBene;
	private String causaleAmmortamentoTipoBene;
	private String testoScritturaAmmortamento;
	private String contoAmmortamentoTipoBene;
	private String contoFondoAmmortamentoTipoBene;
	private String contoPlusvalenzaTipoBene;
	private String contoMinusvalenzaTipoBene;
	private String eventoIncrementoTipoBene;
	private String causaleIncrementoTipoBene;
	private String contoIncrementoTipoBene;
	private String eventoDecrementoTipoBene;
	private String causaleDecrementoTipoBene;
	private String contoDecrementoTipoBene;
	private String contoAlienazioneTipoBene;
	private String contoDonazioneTipoBene;
	

	/**
	 * Instantiates a new consulta tipo bene model.
	 */
	public ConsultaTipoBeneModel() {
		setTitolo("consulta tipo bene");
	}
	
	

	/**
	 * @return the categoriaCespite
	 */
	public String getCategoriaCespite() {
		return categoriaCespite;
	}



	/**
	 * @param categoriaCespite the categoriaCespite to set
	 */
	public void setCategoriaCespite(String categoriaCespite) {
		this.categoriaCespite = categoriaCespite;
	}



	/**
	 * @return the contoPatrimonialeTipoBene
	 */
	public String getContoPatrimonialeTipoBene() {
		return contoPatrimonialeTipoBene;
	}



	/**
	 * @param contoPatrimonialeTipoBene the contoPatrimonialeTipoBene to set
	 */
	public void setContoPatrimonialeTipoBene(String contoPatrimonialeTipoBene) {
		this.contoPatrimonialeTipoBene = contoPatrimonialeTipoBene;
	}



	/**
	 * @return the eventoAmmortamentoTipoBene
	 */
	public String getEventoAmmortamentoTipoBene() {
		return eventoAmmortamentoTipoBene;
	}



	/**
	 * @param eventoAmmortamentoTipoBene the eventoAmmortamentoTipoBene to set
	 */
	public void setEventoAmmortamentoTipoBene(String eventoAmmortamentoTipoBene) {
		this.eventoAmmortamentoTipoBene = eventoAmmortamentoTipoBene;
	}



	/**
	 * @return the causaleAmmortamentoTipoBene
	 */
	public String getCausaleAmmortamentoTipoBene() {
		return causaleAmmortamentoTipoBene;
	}



	/**
	 * @param causaleAmmortamentoTipoBene the causaleAmmortamentoTipoBene to set
	 */
	public void setCausaleAmmortamentoTipoBene(String causaleAmmortamentoTipoBene) {
		this.causaleAmmortamentoTipoBene = causaleAmmortamentoTipoBene;
	}



	/**
	 * @return the testoScritturaAmmortamento
	 */
	public String getTestoScritturaAmmortamento() {
		return testoScritturaAmmortamento;
	}



	/**
	 * @param testoScritturaAmmortamento the testoScritturaAmmortamento to set
	 */
	public void setTestoScritturaAmmortamento(String testoScritturaAmmortamento) {
		this.testoScritturaAmmortamento = testoScritturaAmmortamento;
	}



	/**
	 * @return the contoAmmortamentoTipoBene
	 */
	public String getContoAmmortamentoTipoBene() {
		return contoAmmortamentoTipoBene;
	}



	/**
	 * @param contoAmmortamentoTipoBene the contoAmmortamentoTipoBene to set
	 */
	public void setContoAmmortamentoTipoBene(String contoAmmortamentoTipoBene) {
		this.contoAmmortamentoTipoBene = contoAmmortamentoTipoBene;
	}



	/**
	 * @return the contoFondoAmmortamentoTipoBene
	 */
	public String getContoFondoAmmortamentoTipoBene() {
		return contoFondoAmmortamentoTipoBene;
	}



	/**
	 * @param contoFondoAmmortamentoTipoBene the contoFondoAmmortamentoTipoBene to set
	 */
	public void setContoFondoAmmortamentoTipoBene(String contoFondoAmmortamentoTipoBene) {
		this.contoFondoAmmortamentoTipoBene = contoFondoAmmortamentoTipoBene;
	}



	/**
	 * @return the contoPlusvalenzaTipoBene
	 */
	public String getContoPlusvalenzaTipoBene() {
		return contoPlusvalenzaTipoBene;
	}



	/**
	 * @param contoPlusvalenzaTipoBene the contoPlusvalenzaTipoBene to set
	 */
	public void setContoPlusvalenzaTipoBene(String contoPlusvalenzaTipoBene) {
		this.contoPlusvalenzaTipoBene = contoPlusvalenzaTipoBene;
	}



	/**
	 * @return the contoMinusvalenzaTipoBene
	 */
	public String getContoMinusvalenzaTipoBene() {
		return contoMinusvalenzaTipoBene;
	}



	/**
	 * @param contoMinusvalenzaTipoBene the contoMinusvalenzaTipoBene to set
	 */
	public void setContoMinusvalenzaTipoBene(String contoMinusvalenzaTipoBene) {
		this.contoMinusvalenzaTipoBene = contoMinusvalenzaTipoBene;
	}



	/**
	 * @return the eventoIncrementoTipoBene
	 */
	public String getEventoIncrementoTipoBene() {
		return eventoIncrementoTipoBene;
	}



	/**
	 * @param eventoIncrementoTipoBene the eventoIncrementoTipoBene to set
	 */
	public void setEventoIncrementoTipoBene(String eventoIncrementoTipoBene) {
		this.eventoIncrementoTipoBene = eventoIncrementoTipoBene;
	}



	/**
	 * @return the causaleIncrementoTipoBene
	 */
	public String getCausaleIncrementoTipoBene() {
		return causaleIncrementoTipoBene;
	}



	/**
	 * @param causaleIncrementoTipoBene the causaleIncrementoTipoBene to set
	 */
	public void setCausaleIncrementoTipoBene(String causaleIncrementoTipoBene) {
		this.causaleIncrementoTipoBene = causaleIncrementoTipoBene;
	}



	/**
	 * @return the contoIncrementoTipoBene
	 */
	public String getContoIncrementoTipoBene() {
		return contoIncrementoTipoBene;
	}



	/**
	 * @param contoIncrementoTipoBene the contoIncrementoTipoBene to set
	 */
	public void setContoIncrementoTipoBene(String contoIncrementoTipoBene) {
		this.contoIncrementoTipoBene = contoIncrementoTipoBene;
	}



	/**
	 * @return the eventoDecrementoTipoBene
	 */
	public String getEventoDecrementoTipoBene() {
		return eventoDecrementoTipoBene;
	}



	/**
	 * @param eventoDecrementoTipoBene the eventoDecrementoTipoBene to set
	 */
	public void setEventoDecrementoTipoBene(String eventoDecrementoTipoBene) {
		this.eventoDecrementoTipoBene = eventoDecrementoTipoBene;
	}



	/**
	 * @return the causaleDecrementoTipoBene
	 */
	public String getCausaleDecrementoTipoBene() {
		return causaleDecrementoTipoBene;
	}



	/**
	 * @param causaleDecrementoTipoBene the causaleDecrementoTipoBene to set
	 */
	public void setCausaleDecrementoTipoBene(String causaleDecrementoTipoBene) {
		this.causaleDecrementoTipoBene = causaleDecrementoTipoBene;
	}



	/**
	 * @return the contoDecrementoTipoBene
	 */
	public String getContoDecrementoTipoBene() {
		return contoDecrementoTipoBene;
	}



	/**
	 * @param contoDecrementoTipoBene the contoDecrementoTipoBene to set
	 */
	public void setContoDecrementoTipoBene(String contoDecrementoTipoBene) {
		this.contoDecrementoTipoBene = contoDecrementoTipoBene;
	}



	/**
	 * @return the contoAlienazioneTipoBene
	 */
	public String getContoAlienazioneTipoBene() {
		return contoAlienazioneTipoBene;
	}



	/**
	 * @param contoAlienazioneTipoBene the contoAlienazioneTipoBene to set
	 */
	public void setContoAlienazioneTipoBene(String contoAlienazioneTipoBene) {
		this.contoAlienazioneTipoBene = contoAlienazioneTipoBene;
	}



	/**
	 * @return the contoDonazioneTipoBene
	 */
	public String getContoDonazioneTipoBene() {
		return contoDonazioneTipoBene;
	}



	/**
	 * @param contoDonazioneTipoBene the contoDonazioneTipoBene to set
	 */
	public void setContoDonazioneTipoBene(String contoDonazioneTipoBene) {
		this.contoDonazioneTipoBene = contoDonazioneTipoBene;
	}



	/**
	 * Imposta dati nel model.
	 *
	 * @param tipoBene the tipo bene
	 */
	public void impostaDatiNelModel(TipoBeneCespite tipoBene) {
		setTipoBeneCespite(tipoBene);
		
		impostaCategoriaCespiti(tipoBene.getCategoriaCespiti());
		
		setContoPatrimonialeTipoBene(creaStringaByConto(tipoBene.getContoPatrimoniale()));
		setTestoScritturaAmmortamento(StringUtils.isNotBlank(tipoBene.getTestoScritturaAmmortamento()) ? tipoBene.getTestoScritturaAmmortamento() : "");
		
		setContoAmmortamentoTipoBene(creaStringaByConto(tipoBene.getContoAmmortamento()));
		setEventoAmmortamentoTipoBene(creaStringaByEvento(tipoBene.getEventoAmmortamento()));
		setCausaleAmmortamentoTipoBene(creaStringaByCausale(tipoBene.getCausaleAmmortamento()));
		
		setContoFondoAmmortamentoTipoBene(creaStringaByConto(tipoBene.getContoFondoAmmortamento()));
		
		setContoIncrementoTipoBene(creaStringaByConto(tipoBene.getContoIncremento()));
		setEventoIncrementoTipoBene(creaStringaByEvento(tipoBene.getEventoIncremento()));
		setCausaleIncrementoTipoBene(creaStringaByCausale(tipoBene.getCausaleIncremento()));
		
		
		setContoDecrementoTipoBene(creaStringaByConto(tipoBene.getContoDecremento()));
		setEventoDecrementoTipoBene(creaStringaByEvento(tipoBene.getEventoDecremento()));
		setCausaleDecrementoTipoBene(creaStringaByCausale(tipoBene.getCausaleDecremento()));
		
		setContoAlienazioneTipoBene(creaStringaByConto(tipoBene.getContoAlienazione()));
		setContoDonazioneTipoBene(creaStringaByConto(tipoBene.getContoDonazione()));
		
		setContoPlusvalenzaTipoBene(creaStringaByConto(tipoBene.getContoPlusvalenza()));
		setContoMinusvalenzaTipoBene(creaStringaByConto(tipoBene.getContoMinusValenza()));
	}



	/**
	 * @param categoriaCespiti
	 */
	private void impostaCategoriaCespiti(CategoriaCespiti categoriaCespiti) {
		if(categoriaCespiti == null) {
			return ;
		}
		
		StringBuilder cat = new StringBuilder()
		.append(StringUtils.isNotBlank(categoriaCespiti.getCodice())? categoriaCespiti.getCodice() : "")
		.append(" - ")
		.append(StringUtils.isNotBlank(categoriaCespiti.getDescrizione())? categoriaCespiti.getDescrizione() : "")
		;
		setCategoriaCespite(cat.toString());
	}
	
	/**
	 * Crea stringa by conto.
	 *
	 * @param conto the conto
	 * @return the string
	 */
	private String creaStringaByConto(Conto conto) {
		if(conto == null) {
			return ""; 
		}
		
		return new StringBuilder()
				.append(StringUtils.isNotBlank(conto.getCodice())? conto.getCodice() : "")
				.append(" - ")
				.append(StringUtils.isNotBlank(conto.getDescrizione())? conto.getDescrizione() : "")
				.toString();
	}
	
	/**
	 * Crea stringa by evento.
	 *
	 * @param evento the evento
	 * @return the string
	 */
	private String creaStringaByEvento(Evento evento) {
		if(evento == null) {
			return ""; 
		}
		
		return new StringBuilder()
				.append(StringUtils.isNotBlank(evento.getCodice())? evento.getCodice() : "")
				.append(" - ")
				.append(StringUtils.isNotBlank(evento.getDescrizione())? evento.getDescrizione() : "")
				.toString();
	}
	
	/**
	 * Crea stringa by causale.
	 *
	 * @param causale the causale
	 * @return the string
	 */
	private String creaStringaByCausale(CausaleEP causale) {
		if(causale == null) {
			return ""; 
		}
		
		return new StringBuilder()
				.append(StringUtils.isNotBlank(causale.getCodice())? causale.getCodice() : "")
				.append(" - ")
				.append(StringUtils.isNotBlank(causale.getDescrizione())? causale.getDescrizione() : "")
				.toString();
	}
	

}
