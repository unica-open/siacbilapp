/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.risultatiricerca;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacgenser.model.RateoRisconto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * Wrapper per la registrazioneMovFin.
 * 
 * @author Valentina
 * @version 1.0.0 - 04/05/2015
 *
 */
public class ElementoRegistrazioneMovFin implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4211692892934927840L;
	/** La registrazione */
	protected RegistrazioneMovFin registrazioneMovFin;
	private String azioni;
	
	/**
	 * Costruttore di wrap.
	 * 
	 * @param registrazioneMovFin la registrazione da wrappare
	 */
	public ElementoRegistrazioneMovFin(RegistrazioneMovFin registrazioneMovFin) {
		this.registrazioneMovFin = registrazioneMovFin;
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
	
	@Override
	public int getUid() {
		return registrazioneMovFin != null ? registrazioneMovFin.getUid() : -1;
	}
	
//	/**
//	 * @return the stringaMovimento
//	 */
//	public abstract String getStringaMovimento();
	
	/**
	 * @return the stringaMovimento
	 */
	public String getStringaMovimento(){
		if(registrazioneMovFin == null || registrazioneMovFin.getEvento() == null || registrazioneMovFin.getMovimento() == null) {
			return "";
		}
		TipoCollegamento tipoCollegamento = registrazioneMovFin.getEvento().getTipoCollegamento();
		
		if(TipoCollegamento.DOCUMENTO_SPESA.equals(tipoCollegamento)){
			DocumentoSpesa docSpesa = (DocumentoSpesa) registrazioneMovFin.getMovimento();
			return docSpesa.getAnno() + "/" + docSpesa.getNumero() + "/" +
					docSpesa.getTipoDocumento().getCodice() + "/" + docSpesa.getSoggetto().getCodiceSoggetto() + " " +
					docSpesa.getSoggetto().getDenominazione();
		}
		if(TipoCollegamento.DOCUMENTO_ENTRATA.equals(tipoCollegamento)){
			DocumentoEntrata docEntrata = (DocumentoEntrata) registrazioneMovFin.getMovimento();
			return docEntrata.getAnno() + "/" + docEntrata.getNumero() + "/" +
					docEntrata.getTipoDocumento().getCodice() + "/" + docEntrata.getSoggetto().getCodiceSoggetto()+ " " +
					docEntrata.getSoggetto().getDenominazione();
		}
		if(TipoCollegamento.SUBDOCUMENTO_SPESA.equals(tipoCollegamento)){
			SubdocumentoSpesa subdocSpesa = (SubdocumentoSpesa) registrazioneMovFin.getMovimento();
			DocumentoSpesa docSpesa = subdocSpesa.getDocumento();
			return docSpesa.getAnno() + "/" + docSpesa.getNumero() + "/" + subdocSpesa.getNumero() + "/" +
					docSpesa.getTipoDocumento().getCodice() + "/" + docSpesa.getSoggetto().getCodiceSoggetto()+ " " +
					docSpesa.getSoggetto().getDenominazione();
		}
		if(TipoCollegamento.SUBDOCUMENTO_ENTRATA.equals(tipoCollegamento)){
			SubdocumentoEntrata subdocEntrata = (SubdocumentoEntrata) registrazioneMovFin.getMovimento();
			DocumentoEntrata docEntrata = subdocEntrata.getDocumento();
			return docEntrata.getAnno() + "/" + docEntrata.getNumero() + "/" + subdocEntrata.getNumero() + "/" +
					docEntrata.getTipoDocumento().getCodice() + "/" + docEntrata.getSoggetto().getCodiceSoggetto()+ " " +
					docEntrata.getSoggetto().getDenominazione();
		}
		if(TipoCollegamento.IMPEGNO.equals(tipoCollegamento)){
			Impegno impegno = (Impegno) registrazioneMovFin.getMovimento();
			return impegno.getAnnoMovimento() + "/" + impegno.getNumeroBigDecimal();
		}
		if(TipoCollegamento.ACCERTAMENTO.equals(tipoCollegamento)){
			Accertamento accertamento = (Accertamento) registrazioneMovFin.getMovimento();
			return accertamento.getAnnoMovimento() + "/" + accertamento.getNumeroBigDecimal();
		}
		if(TipoCollegamento.SUBIMPEGNO.equals(tipoCollegamento)){
			SubImpegno subImpegno = (SubImpegno) registrazioneMovFin.getMovimento();
			return subImpegno.getAnnoMovimento() + "/" + subImpegno.getNumeroImpegnoPadre() + "-" + subImpegno.getNumeroBigDecimal();
		}
		if(TipoCollegamento.SUBACCERTAMENTO.equals(tipoCollegamento)){
			SubAccertamento subAccertamento = (SubAccertamento) registrazioneMovFin.getMovimento();
			return subAccertamento.getAnnoMovimento() + "/" + subAccertamento.getNumeroAccertamentoPadre() + "-" + subAccertamento.getNumeroBigDecimal();
		}
		if(TipoCollegamento.LIQUIDAZIONE.equals(tipoCollegamento)){
			Liquidazione liquidazione = (Liquidazione) registrazioneMovFin.getMovimento();
			return liquidazione.getAnnoLiquidazione() + "/" + liquidazione.getNumeroLiquidazione();
		}
		if(TipoCollegamento.ORDINATIVO_PAGAMENTO.equals(tipoCollegamento)){
			Ordinativo ordPagamento = (Ordinativo) registrazioneMovFin.getMovimento();
			return ordPagamento.getAnno() + "/" + ordPagamento.getNumero();
		}
		if(TipoCollegamento.ORDINATIVO_INCASSO.equals(tipoCollegamento)){
			Ordinativo ordIncasso = (Ordinativo) registrazioneMovFin.getMovimento();
			return ordIncasso.getAnno() + "/" + ordIncasso.getNumero();
		}
		if(TipoCollegamento.ORDINATIVO_INCASSO.equals(tipoCollegamento)){
			Ordinativo ordIncasso = (Ordinativo) registrazioneMovFin.getMovimento();
			return ordIncasso.getAnno() + "/" + ordIncasso.getNumero();
		}
		if(TipoCollegamento.RICHIESTA_ECONOMALE.equals(tipoCollegamento)) {
			RichiestaEconomale richiestaEconomale = (RichiestaEconomale) registrazioneMovFin.getMovimento();
			return FormatUtils.formatDateYear(richiestaEconomale.getMovimento().getDataMovimento()) + "/" + richiestaEconomale.getMovimento().getNumeroMovimento().toString();
		}
		if(TipoCollegamento.RENDICONTO_RICHIESTA.equals(tipoCollegamento)) {
			RendicontoRichiesta rendicontoRichiesta = (RendicontoRichiesta) registrazioneMovFin.getMovimento();
			return FormatUtils.formatDateYear(rendicontoRichiesta.getMovimento().getDataMovimento()) + "/" + rendicontoRichiesta.getMovimento().getNumeroMovimento().toString();
		}
		
		// SIAC-2495
		if(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_SPESA.equals(tipoCollegamento)) {
			ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa = (ModificaMovimentoGestioneSpesa) registrazioneMovFin.getMovimento();
			if(modificaMovimentoGestioneSpesa != null && modificaMovimentoGestioneSpesa.getImpegno() != null) {
				return modificaMovimentoGestioneSpesa.getImpegno().getAnnoMovimento() + "/" + modificaMovimentoGestioneSpesa.getImpegno().getNumeroBigDecimal();
			}
			if(modificaMovimentoGestioneSpesa != null && modificaMovimentoGestioneSpesa.getSubImpegno() != null) {
				return modificaMovimentoGestioneSpesa.getSubImpegno().getAnnoImpegnoPadre() + "/" + modificaMovimentoGestioneSpesa.getSubImpegno().getNumeroImpegnoPadre()
						+ "-" + modificaMovimentoGestioneSpesa.getSubImpegno().getNumeroBigDecimal();
			}
		}
		if(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA.equals(tipoCollegamento)) {
			ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = (ModificaMovimentoGestioneEntrata) registrazioneMovFin.getMovimento();
			if(modificaMovimentoGestioneEntrata != null && modificaMovimentoGestioneEntrata.getAccertamento() != null) {
				return modificaMovimentoGestioneEntrata.getAccertamento().getAnnoMovimento() + "/" + modificaMovimentoGestioneEntrata.getAccertamento().getNumeroBigDecimal();
			}
			if(modificaMovimentoGestioneEntrata != null && modificaMovimentoGestioneEntrata.getSubAccertamento() != null) {
				return modificaMovimentoGestioneEntrata.getSubAccertamento().getAnnoAccertamentoPadre() + "/" + modificaMovimentoGestioneEntrata.getSubAccertamento().getNumeroAccertamentoPadre()
						+ "-" + modificaMovimentoGestioneEntrata.getSubAccertamento().getNumeroBigDecimal();
			}
		}
		if(TipoCollegamento.RATEO.equals(tipoCollegamento) || TipoCollegamento.RISCONTO.equals(tipoCollegamento)){
			RateoRisconto rateoRisconto = (RateoRisconto)registrazioneMovFin.getMovimento();
			return rateoRisconto.getAnno()+"";
		}
		
		return "";
	}
	
	/**
	 * @return the stringaStatoOperativoRegistrazioneMovFin
	 */
	public String getStringaStatoOperativoRegistrazioneMovFin() {
		return registrazioneMovFin != null && registrazioneMovFin.getStatoOperativoRegistrazioneMovFin() != null ? registrazioneMovFin.getStatoOperativoRegistrazioneMovFin().getDescrizione() : "";
	}
	
	/**
	 * @return the stringaEvento
	 */
	public String getStringaEvento() {
		return registrazioneMovFin != null && registrazioneMovFin.getEvento() != null ? registrazioneMovFin.getEvento().getCodice() : "";
	}
	
	/**
	 * @return the stringaDataRegistrazione
	 */
	public String getStringaDataRegistrazione() {
		return registrazioneMovFin != null ? FormatUtils.formatDate(registrazioneMovFin.getDataRegistrazione()) : "";
	}
	
	/**
	 * @return the stringaConto
	 */
	public String getStringaContoAggiornato() {
		
		if(isContoInizialeUgualeContoAggiornato()){
			return "";
		}
		
		return registrazioneMovFin != null && registrazioneMovFin.getElementoPianoDeiContiAggiornato()  != null ? registrazioneMovFin.getElementoPianoDeiContiAggiornato().getCodice() : "";
	}
	
	/**
	 * @return the stringaElementoPianoDeiConti
	 */
	public String getStringaContoIniziale() {
		return registrazioneMovFin != null && registrazioneMovFin.getElementoPianoDeiContiIniziale() != null ? registrazioneMovFin.getElementoPianoDeiContiIniziale().getCodice() : "";
	}
	/**
	 * @return the uidPianoDeicontiFIN
	 */
	public int getUidPianoDeiContiFIN(){
		return registrazioneMovFin != null && registrazioneMovFin.getElementoPianoDeiContiAggiornato() != null ? registrazioneMovFin.getElementoPianoDeiContiAggiornato().getUid() : 0;
	}
	
	/**
	 * @return the tipoCollegamento
	 */
	public TipoCollegamento getTipoCollegamento() {
		return registrazioneMovFin != null && registrazioneMovFin.getEvento() != null ? registrazioneMovFin.getEvento().getTipoCollegamento() : null;
	}
	
	/**
	 * Ottiene il movimento castato correttamente.
	 * @param <T> la tipizzazione del movimento
	 * 
	 * @return il movimento
	 */
	@SuppressWarnings("unchecked")
	public <T> T obtainMovimentoCast() {
		return registrazioneMovFin != null ? (T)registrazioneMovFin.getMovimento() : null;
	}
	
	/**
	 * @return movimentoSubdocumentoWithDocumento
	 */
	public boolean isMovimentoSubdocumentoWithDocumento() {
		return registrazioneMovFin != null && registrazioneMovFin.getMovimento() instanceof Subdocumento && ((Subdocumento<?, ?>)registrazioneMovFin.getMovimento()).getDocumento() != null
				&& ((Subdocumento<?, ?>)registrazioneMovFin.getMovimento()).getDocumento().getUid() != 0;
	}
	
	/**
	 * @return the uidMovimento
	 */
	public int getUidMovimento() {
		return registrazioneMovFin != null && registrazioneMovFin.getMovimento() != null ? registrazioneMovFin.getMovimento().getUid() : 0;
	}
	
	/**
	 * @return the codiceEvento
	 */
	public String getCodiceEvento() {
		return registrazioneMovFin != null && registrazioneMovFin.getEvento() != null ? registrazioneMovFin.getEvento().getCodice() : "";
	}
	
	/**
	 * @return the codiceTipoEvento
	 */
	public String getCodiceTipoEvento() {
		return registrazioneMovFin != null && registrazioneMovFin.getEvento() != null && registrazioneMovFin.getEvento().getTipoEvento() != null
				? registrazioneMovFin.getEvento().getTipoEvento().getCodice()
				: "";
	}
	
	/**
	 * @return the codiceTipoCollegamento
	 */
	public String getCodiceTipoCollegamento() {
		return registrazioneMovFin != null && registrazioneMovFin.getEvento() != null && registrazioneMovFin.getEvento().getTipoCollegamento() != null
				? registrazioneMovFin.getEvento().getTipoCollegamento().getCodice()
				: "";
	}
	
	/**
	 * @return the statoOperativoRegistrazioneMovFin
	 */
	public StatoOperativoRegistrazioneMovFin getStatoOperativoRegistrazioneMovFin() {
		return registrazioneMovFin != null ? registrazioneMovFin.getStatoOperativoRegistrazioneMovFin() : null;
	}
	
	/**w
	 * @return the tipoElenco
	 */
	public String getTipoElenco(){
		if(registrazioneMovFin == null || registrazioneMovFin.getEvento() == null || registrazioneMovFin.getEvento().getTipoEvento() == null) {
			return "";
		}
		
		//alcuni eventi non sono ne' entrata ne' spesa, per esempio ratei e risconti. compaioni in entrambi gli elenchi
		if(registrazioneMovFin.getEvento().getTipoEvento().isTipoSpesa() && registrazioneMovFin.getEvento().getTipoEvento().isTipoEntrata()){
			return "";
		}
		return registrazioneMovFin.getEvento().getTipoEvento().isTipoSpesa() ? "Spesa" : "Entrata";
	}
	
	/**
	 * @return eventoMotaCredito
	 */
	public boolean isEventoNotaCredito(){
		return registrazioneMovFin != null && registrazioneMovFin.getMovimentoCollegato() != null;
		
//		Evento evento = registrazioneMovFin.getEvento();
//		//Arrays.asList("NCD-INS","NCD-AGG", "NDI-INS", "NDI-AGG", "NCP-INS", "NCP-AGG", "NCE-INS", "NCE-AGG", "NCV-INS", "NCV-INS", "NEP-INS");
//		return evento.getCodice().startsWith("N");
	}
	
	/**
	 * @return the uidPrimaNota
	 */
	public String getUidPrimaNota(){
		if(registrazioneMovFin.getMovimento() instanceof RateoRisconto){
			RateoRisconto rateoRisconto = (RateoRisconto) registrazioneMovFin.getMovimento();
			return rateoRisconto.getPrimaNota().getUid()+"";
		}
		return "";
	}
	
	/**
	 * @return the isResiduo
	 */
	public String getIsResiduo() {
		return registrazioneMovFin != null && Boolean.TRUE.equals(registrazioneMovFin.getIsCollegataAMovimentoResiduo())
			? "S&iacute;"
			: "No";
	}
	
	/**
	 * Controlla se il conto iniziale sia uguale al conto aggiornato
	 * @return <code>true</code> se i conti sono uguali; <code>false</code> altrimenti
	 */
	private boolean isContoInizialeUgualeContoAggiornato(){
		//conto iniziale dovrebbe essere sempre !=null
		return registrazioneMovFin !=null && registrazioneMovFin.getElementoPianoDeiContiAggiornato() !=null 
				&&  registrazioneMovFin.getElementoPianoDeiContiIniziale().getUid() == registrazioneMovFin.getElementoPianoDeiContiAggiornato().getUid();
	}
}
