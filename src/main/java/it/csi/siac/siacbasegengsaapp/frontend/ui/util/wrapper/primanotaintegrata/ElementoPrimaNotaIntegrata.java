/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.movimento.RegistrazioneMovFinMovimentoCollegatoHelper;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.Rateo;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.Risconto;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Wrapper per la prima nota integrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/05/2015
 *
 */
public class ElementoPrimaNotaIntegrata implements Serializable, ModelWrapper {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6274570756468508353L;
	
	private final PrimaNota primaNota;
	private String azioni;
	 
	/**
	 * Costruttore di wrap.
	 * 
	 * @param primaNota la primaNota da wrappare
	 */
	public ElementoPrimaNotaIntegrata(PrimaNota primaNota) {
		this.primaNota = primaNota;
	}

	/**
	 * @return the azioni
	 */
	public String getAzioni() {
		return azioni;
	}

	/**
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}
	
	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return primaNota != null ? primaNota.getDescrizione() : "";
	}
	
	/**
	 * @return the numero
	 */
	public String getNumero() {
		return primaNota != null && primaNota.getNumero() != null ? primaNota.getNumero().toString() : "";
	}
	
	/**
	 * @return the NumeroLibroGiornale
	 */
	public String getNumeroLibroGiornale() {
		return primaNota != null && primaNota.getNumeroRegistrazioneLibroGiornale() != null ? primaNota.getNumeroRegistrazioneLibroGiornale().toString() : "";
	}
	
	
	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return primaNota != null && primaNota.getTipoCausale() != null ? primaNota.getTipoCausale().getDescrizione() : "";
	}
	
	/**
	 * @return the numeroMovimento
	 */
	public String getNumeroMovimento() {
		return RegistrazioneMovFinMovimentoCollegatoHelper.getNumeroMovimento(primaNota);
//		RegistrazioneMovFin registrazioneMovimentoFin = getRegistrazioneMovFin();
//		
//		if(registrazioneMovimentoFin == null) {
//			// Se non ho la registrazione, esco
//			return "";
//		}
//		
//		Entita entita = registrazioneMovimentoFin.getMovimento();
//		
//		if(entita instanceof ModificaMovimentoGestione){
//			return calcolaNumeroMovimentoPerEntita((ModificaMovimentoGestione)entita);
//		}
//		if(entita instanceof MovimentoGestione) {
//			return calcolaNumeroMovimentoPerEntita((MovimentoGestione)entita);
//		}
//		if(entita instanceof Documento) {
//			return calcolaNumeroMovimentoPerEntita((Documento<?, ?>)entita);
//		}
//		if(entita instanceof Subdocumento) {
//			Subdocumento<?, ?> subdocumento = (Subdocumento<?, ?>)entita;
//			return calcolaNumeroMovimentoPerEntita(subdocumento.getDocumento());
//		}
//		if(entita instanceof Liquidazione) {
//			return calcolaNumeroMovimentoPerEntita((Liquidazione)entita);
//		}
//		if(entita instanceof Ordinativo) {
//			return calcolaNumeroMovimentoPerEntita((Ordinativo)entita);
//		}
//		if(entita instanceof RichiestaEconomale) {
//			return calcolaNumeroMovimentoPerEntita((RichiestaEconomale)entita);
//		}
//		if(entita instanceof RendicontoRichiesta) {
//			return calcolaNumeroMovimentoPerEntita((RendicontoRichiesta)entita);
//		}
//		if(entita instanceof Rateo){
//			return calcolaNumeroMovimentoPerEntita((Rateo)entita);
//		}
//		if(entita instanceof Risconto){
//			return calcolaNumeroMovimentoPerEntita((Risconto)entita);
//		}
//		
//		return "";
	}
	
	
//	/**
//	 * Calcolo del numero di movimento per ModificaMovimentoGestione.
//	 * <br/>
//	 * Anno + numero + submovimento (se presente) per i movimenti.
//	 * 
//	 * @param modificaMovimentoGestione la modifica del movimento di gestione per cui calcolare il numero di movimento
//	 * @return il numero
//	 */
//	private String calcolaNumeroMovimentoPerEntita(ModificaMovimentoGestione modificaMovimentoGestione) {
//		StringBuilder sb = new StringBuilder();
//		
//		int anno = 0;
//		BigDecimal numero = null;
//		BigDecimal numeroSub = null;
//		String terminal = "";
//		
//		if(modificaMovimentoGestione instanceof ModificaMovimentoGestioneSpesa){
//			ModificaMovimentoGestioneSpesa mod = (ModificaMovimentoGestioneSpesa)modificaMovimentoGestione;
//			terminal = " (imp)";
//			Impegno impegno = mod.getImpegno();
//			SubImpegno subimpegno = mod.getSubImpegno();
//			if(impegno != null){
//				numero = impegno.getNumero();
//				anno = impegno.getAnnoMovimento();
//			}else if(subimpegno != null){
//				numero = subimpegno.getNumeroImpegnoPadre();
//				numeroSub = subimpegno.getNumero();
//				anno = subimpegno.getAnnoImpegnoPadre();
//			}
//		}
//		
//		if(modificaMovimentoGestione instanceof ModificaMovimentoGestioneEntrata){
//			ModificaMovimentoGestioneEntrata mod = (ModificaMovimentoGestioneEntrata)modificaMovimentoGestione;
//			terminal = " (acc)";
//			Accertamento accertamento = mod.getAccertamento();
//			SubAccertamento subaccertamento = mod.getSubAccertamento();
//			if(accertamento != null){
//				numero = accertamento.getNumero();
//				anno = accertamento.getAnnoMovimento();
//			}else if(subaccertamento != null){
//				numero = subaccertamento.getNumeroAccertamentoPadre();
//				numeroSub = subaccertamento.getNumero();
//				anno = subaccertamento.getAnnoAccertamentoPadre();
//			}
//		}
//		
//		sb.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
//			.append(FormatUtils.formatHtmlAttributeString(modificaMovimentoGestione.getDescrizioneModificaMovimentoGestione()))
//			.append("\" data-html=\"true\">")
//			.append(anno);
//		
//		// Numero
//		if(numero != null) {
//			sb.append(" / ")
//				.append(numero.toPlainString());
//		}
//		if(numeroSub != null) {
//			sb.append(" - ")
//				.append(numeroSub.toPlainString());
//		}
//		
//		sb.append(terminal)
//			.append("</a>");
//		return sb.toString();
//	}
//	
//	/**
//	 * Calcolo del numero di movimento per il movimento di gestione.
//	 * <br/>
//	 * Anno + numero + submovimento (se presente) per i movimenti.
//	 * 
//	 * @param movimentoGestione il movimento di gestione per cui calcolare il numero di movimento
//	 * @return il numero
//	 */
//	private String calcolaNumeroMovimentoPerEntita(MovimentoGestione movimentoGestione) {
//		StringBuilder sb = new StringBuilder();
//		
//		sb.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
//			.append(movimentoGestione.getDescrizione() != null ? FormatUtils.formatHtmlAttributeString(movimentoGestione.getDescrizione()) : "")
//			.append("\" data-html=\"true\">")
//			.append(movimentoGestione.getAnnoMovimento());
//		
//		BigDecimal numero = movimentoGestione.getNumero();
//		BigDecimal numeroSub = null;
//		String terminal = "";
//		
//		if(movimentoGestione instanceof Impegno) {
//			terminal = " (imp)";
//			
//			if(movimentoGestione instanceof SubImpegno) {
//				numero = ((SubImpegno) movimentoGestione).getNumeroImpegnoPadre();
//				numeroSub = movimentoGestione.getNumero();
//			}
//		} else if(movimentoGestione instanceof Accertamento) {
//			terminal = " (acc)";
//			
//			if(movimentoGestione instanceof SubAccertamento) {
//				numero = ((SubAccertamento) movimentoGestione).getNumeroAccertamentoPadre();
//				numeroSub = movimentoGestione.getNumero();
//			}
//		}
//		
//		// Numero
//		if(numero != null) {
//			sb.append(" / ")
//				.append(numero.toPlainString());
//		}
//		if(numeroSub != null) {
//			sb.append(" - ")
//				.append(numeroSub.toPlainString());
//		}
//		
//		sb.append(terminal)
//			.append("</a>");
//		return sb.toString();
//	}
//	
//	/**
//	 * Calcolo del numero di movimento per il documento.
//	 * <br/>
//	 * Anno + numero documento + tipo documento + codice soggetto per i documenti.
//	 * 
//	 * @param documento il documento per cui calcolare il numero di movimento
//	 * @return il numero
//	 */
//	private String calcolaNumeroMovimentoPerEntita(Documento<?, ?> documento) {
//		StringBuilder sb = new StringBuilder();
//		
//		sb.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
//			.append(FormatUtils.formatHtmlAttributeString(documento.getDescrizione()))
//			.append("\" data-html=\"true\">")
//			.append(documento.getAnno())
//			.append(" / ")
//			.append(documento.getNumero());
//		if(documento.getTipoDocumento() != null) {
//			sb.append(" / ")
//				.append(documento.getTipoDocumento().getCodice());
//		}
//		if(documento.getSoggetto() != null) {
//			sb.append(" / ")
//				.append(documento.getSoggetto().getCodiceSoggetto())
//				.append(" ")
//				.append(documento.getSoggetto().getDenominazione());
//		}
//		
//		sb.append("</a>");
//		return sb.toString();
//	}
//	
//	/**
//	 * Calcolo del numero di movimento per la liquidazione.
//	 * <br/>
//	 * Anno + numero per le liquidazioni e gli ordinativi.
//	 * 
//	 * @param liquidazione la liquidazione per cui calcolare il numero di movimento
//	 * @return il numero
//	 */
//	private String calcolaNumeroMovimentoPerEntita(Liquidazione liquidazione) {
//		StringBuilder sb = new StringBuilder();
//		
//		sb.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
//			.append(FormatUtils.formatHtmlAttributeString(liquidazione.getDescrizioneLiquidazione()))
//			.append("\" data-html=\"true\">")
//			.append(liquidazione.getAnnoLiquidazione());
//		
//		if(liquidazione.getNumeroLiquidazione() != null) {
//			sb.append(" / ")
//				.append(liquidazione.getNumeroLiquidazione().toPlainString());
//		}
//		
//		return sb.toString();
//	}
//	
//	/**
//	 * Calcolo del numero di movimento per l'ordinativo.
//	 * <br/>
//	 * Anno + numero per le liquidazioni e gli ordinativi.
//	 * 
//	 * @param ordinativo l'ordinativo per cui calcolare il numero di movimento
//	 * @return il numero
//	 */
//	private String calcolaNumeroMovimentoPerEntita(Ordinativo ordinativo) {
//		StringBuilder sb = new StringBuilder();
//		//SIAC-6002
//		String strMsg = "";
//		String strAsterisco = "";
//		if(ordinativo.getDataSpostamento() != null){
//			strMsg = "<br /><b>Spostata in data </b>" + FormatUtils.formatDate(ordinativo.getDataSpostamento()) ;
//			strAsterisco = "*";
//		}
//		
//		sb.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
//			.append(FormatUtils.formatHtmlAttributeString(ordinativo.getDescrizione()))
//			.append(strMsg)
//			.append("\" data-html=\"true\">")
//			.append(ordinativo.getAnno())
//			.append(" / ")
//			.append(ordinativo.getNumero())
//			.append(strAsterisco)
//			.append("</a>");
//		return sb.toString();
//	}
//	
//	/**
//	 * Calcolo del numero di movimento per la richiesta economale.
//	 * <br/>
//	 * Anno+numero del movimento della richiesta se cassa economale.
//	 * 
//	 * @param richiestaEconomale la richiesta economale per cui calcolare il numero di movimento
//	 * @return il numero
//	 */
//	private String calcolaNumeroMovimentoPerEntita(RichiestaEconomale richiestaEconomale) {
//		if(richiestaEconomale.getImpegno() == null) {
//			return "";
//		}
//		return new StringBuilder()
//			.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
//			.append(FormatUtils.formatHtmlAttributeString(richiestaEconomale.getDescrizioneDellaRichiesta()))
//			.append("\" data-html=\"true\">")
//			.append(richiestaEconomale.getImpegno().getAnnoMovimento())
//			.append(" / ")
//			.append(richiestaEconomale.getImpegno().getNumero() != null ? richiestaEconomale.getImpegno().getNumero().toPlainString() : "")
//			.toString();
//	}
//	
//	/**
//	 * Calcolo del numero di movimento per la richiesta economale.
//	 * <br/>
//	 * Anno+numero del movimento della richiesta se cassa economale.
//	 * 
//	 * @param rendicontoRichiesta la richiesta economale per cui calcolare il numero di movimento
//	 * @return il numero
//	 */
//	private String calcolaNumeroMovimentoPerEntita(RendicontoRichiesta rendicontoRichiesta) {
//		if(rendicontoRichiesta.getImpegno() == null || rendicontoRichiesta.getRichiestaEconomale() == null) {
//			return "";
//		}
//		return new StringBuilder()
//			.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
//			.append(FormatUtils.formatHtmlAttributeString(rendicontoRichiesta.getRichiestaEconomale().getDescrizioneDellaRichiesta()))
//			.append("\" data-html=\"true\">")
//			.append(rendicontoRichiesta.getImpegno().getAnnoMovimento())
//			.append(" / ")
//			.append(rendicontoRichiesta.getImpegno().getNumero() != null ? rendicontoRichiesta.getImpegno().getNumero().toPlainString() : "")
//			.toString();
//	}
//	
//	/**
//	 * Calcolo del numero di movimento per rateo
//	 * <br/>
//	 * Anno del movimento 
//	 * 
//	 * @param rateo il rateo per cui calcolare il numero di movimento
//	 * @return il numero
//	 */
//	private String calcolaNumeroMovimentoPerEntita(Rateo rateo) {
//		//es : "2016 - Risconto (o Rateo) su anno 2017 - Da Prima Nota N. xxx' 
//		StringBuilder sb = new StringBuilder();
//		sb.append(FormatUtils.formatDateYear(primaNota.getDataCreazione()));
//		sb.append(" - rateo ");
//		sb.append("su anno ");
//		sb.append(FormatUtils.formatDateYear(rateo.getDataCreazioneRateoRisconto()));
//		
//		if(primaNota.getListaPrimaNotaPadre() != null  && !primaNota.getListaPrimaNotaPadre().isEmpty()){
//			sb.append(" - ");
//			sb.append("Da Prima Nota ");
//			sb.append(primaNota.getListaPrimaNotaPadre().get(0).getNumeroRegistrazioneLibroGiornale() !=null ? " N.definitivo "+primaNota.getListaPrimaNotaPadre().get(0).getNumeroRegistrazioneLibroGiornale() : "");
//			sb.append(primaNota.getListaPrimaNotaPadre().get(0).getNumero() !=null ? " - provvisorio "+primaNota.getListaPrimaNotaPadre().get(0).getNumero() : "");
//		}
//		return sb.toString();
//	}
//	
//	/**
//	 * Calcolo del numero di movimento per  risconto
//	 * <br/>
//	 * Anno del movimento 
//	 * 
//	 * @param risconto il risconto per cui calcolare il numero di movimento
//	 * @return il numero
//	 */
//	private String calcolaNumeroMovimentoPerEntita(Risconto risconto) {
//		//es : "2016 - Risconto (o Rateo) su anno 2017 - Da Prima Nota N. xxx' 
//		StringBuilder sb = new StringBuilder();
//		sb.append(FormatUtils.formatDateYear(primaNota.getDataCreazione()));
//		sb.append(" - risconto ");
//		sb.append("su anno ");
//		sb.append(FormatUtils.formatDateYear(risconto.getDataCreazioneRateoRisconto()));
//		
//		if(primaNota.getListaPrimaNotaPadre() != null  && !primaNota.getListaPrimaNotaPadre().isEmpty()){
//			sb.append(" - ");
//			sb.append("Da Prima Nota ");
//			sb.append(primaNota.getListaPrimaNotaPadre().get(0).getNumeroRegistrazioneLibroGiornale() !=null ? " N.definitivo "+primaNota.getListaPrimaNotaPadre().get(0).getNumeroRegistrazioneLibroGiornale() : "");
//			sb.append(primaNota.getListaPrimaNotaPadre().get(0).getNumero() !=null ? " - provvisorio "+primaNota.getListaPrimaNotaPadre().get(0).getNumero() : "");
//		}
//		return sb.toString();
//	}
//	
	
	/**
	 * @return tootip per dettagli 
	 */
	public String getInfoENumero() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<a class=\"tooltip-test\" data-original-title=\"Visualizza dettagli\" data-toggle=\"modal\" >")
			.append("<i class=\"icon-info-sign\">&nbsp;<span class=\"nascosto\">")
			.append("Visualizza dettagli")
			.append("</span></i>")
			.append("</a>")
			.append(primaNota.getNumero());
		
		return sb.toString();
	}
	
	/**
	 * @return the dataRegistrazione
	 */
	public String getDataRegistrazione() {
		return primaNota != null ? FormatUtils.formatDate(primaNota.getDataRegistrazioneLibroGiornale()) : "";
	}
	
	/**
	 * @return the dataRegistrazioneDefinitiva
	 */
	public String getDataRegistrazioneDefinitiva() {
		return primaNota != null ? FormatUtils.formatDate(primaNota.getDataRegistrazioneLibroGiornale()) : "";
	}
	
	/**
	 * @return lo statoOperativoPrimaNota
	 */
	public StatoOperativoPrimaNota getStatoOperativoPrimaNota () {
		return primaNota!=null && primaNota.getStatoOperativoPrimaNota() != null ? primaNota.getStatoOperativoPrimaNota() : null;
	}
	
	/**
	 * @return lo stato
	 */
	public String getStato () {
		StatoOperativoPrimaNota statoOperativoPrimaNota = getStatoOperativoPrimaNota();
		return statoOperativoPrimaNota != null ? statoOperativoPrimaNota.getDescrizione() : "";
	}
	
	/**
	 * @return the causaleEPWithPopover
	 */
	public String getCausaleEPWithPopover() {
		StringBuilder sb = new StringBuilder();
		CausaleEP causaleEP = getCausaleEP();
		if(causaleEP != null) {
			sb.append("<a data-original-title=\"Descrizione\" data-trigger=\"hover\" rel=\"popover\" data-placement=\"left\" data-content=\"")
				.append(FormatUtils.formatHtmlAttributeString(causaleEP.getDescrizione()))
				.append("\" >")
				.append(causaleEP.getCodice())
				.append("</a>");
		}
		return sb.toString();
	}
	
	@Override
	public int getUid() {
		return primaNota != null ? primaNota.getUid() : -1;
	}
	
	/**
	 * @return the anno
	 */
	public String getAnno(){
		return primaNota != null && primaNota.getBilancio() != null ? String.valueOf(primaNota.getBilancio().getAnno()) : "";
	}
	
	/**
	 * @return the tipoEvento
	 */
	public String getTipoEvento(){
		if(getCausaleEP() != null && getCausaleEP().getEventi() != null && !getCausaleEP().getEventi().isEmpty()){
			TipoEvento te = getCausaleEP().getEventi().get(0).getTipoEvento();
			return te.getDescrizione();
		}
		return  "";
	}
	
	/**
	 * @return the tipoEvento
	 */
	public String getCodiceTipoEvento(){
		//SIAC-4524
		if(getCausaleEP() != null && getCausaleEP().getEventi() != null && !getCausaleEP().getEventi().isEmpty()){
			TipoEvento te = getCausaleEP().getEventi().get(0).getTipoEvento();
			return te.getCodice();
		}
		return  "";
	}
	
	/**
	 * @return the tipoCausale
	 */
	public TipoCausale getTipoCausale(){
		return  primaNota != null ? primaNota.getTipoCausale() : null;
	}
	
	/**
	 * @return the rateo
	 */
	public Rateo getRateo(){
		return primaNota != null ? primaNota.getRateo() : null;
	}
	
	/**
	 * @return the risconti
	 */
	public List<Risconto> getRisconti(){
		return (primaNota != null && primaNota.getRisconti() != null) ? primaNota.getRisconti() : new ArrayList<Risconto>();
	}
	
	/**
	 * @return the isResiduo
	 */
	public String getIsResiduo() {
		return primaNota != null && Boolean.TRUE.equals(primaNota.getIsCollegataAMovimentoResiduo())
			? "S&iacute;"
			: "No";
	}
	
	/* **** Metodi accessorii **** */
	
	/**
	 * Prima Nota Integrata ha solo un movimentoEP.
	 * 
	 * @return the causaleEP
	 */
	private CausaleEP getCausaleEP() {
		return primaNota != null 
			&& primaNota.getListaMovimentiEP() != null 
			&& !primaNota.getListaMovimentiEP().isEmpty()
			&& primaNota.getListaMovimentiEP().get(0) != null
				? primaNota.getListaMovimentiEP().get(0).getCausaleEP()
				: null;
	}
	
	/**
	 * @return the registrazioneMovFin
	 */
	private RegistrazioneMovFin getRegistrazioneMovFin() {
		return primaNota != null 
			&& primaNota.getListaMovimentiEP() != null 
			&& !primaNota.getListaMovimentiEP().isEmpty()
			&& primaNota.getListaMovimentiEP().get(0) != null
				? primaNota.getListaMovimentiEP().get(0).getRegistrazioneMovFin()
				: null;
	}
	
	/**
	 * Checks se la prima mnota deriva da una NCD.
	 *
	 * @return the string
	 */
	public boolean isPrimaNotaDaNCD() {
		Entita entita = ottieniMovimentoAssociatoAPrimaNota();
		if(entita == null) {
			return false;
		}
		
		if(entita instanceof Documento) {
			return calcolaTipoMovimentoPerEntita((Documento<?, ?>)entita);
		}
		if(entita instanceof Subdocumento) {
			Subdocumento<?, ?> subdocumento = (Subdocumento<?, ?>)entita;
			return calcolaTipoMovimentoPerEntita(subdocumento.getDocumento());
		}
		
		//il movimento non e' relativo ad un documento, esco
		return false;
	}

	/**
	 * @return Entita
	 */
	public Entita ottieniMovimentoAssociatoAPrimaNota() {
		RegistrazioneMovFin registrazioneMovimentoFin = getRegistrazioneMovFin();
		
		if(registrazioneMovimentoFin == null) {
			// Se non ho la registrazione, esco
			return null;
		}
		
		return registrazioneMovimentoFin.getMovimento();
	}

	/**
	 * Calcola tipo movimento per entita.
	 *
	 * @param documento the documento
	 * @return true, if successful
	 */
	private boolean calcolaTipoMovimentoPerEntita(Documento<?, ?> documento) {
		
		if(documento == null || documento.getTipoDocumento() == null) {
			return false;
		}
		
		return documento.getTipoDocumento().isNotaCredito();
	}
	
}
