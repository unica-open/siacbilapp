/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.liquidazione;

import java.util.Date;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.liquidazione.GestioneLiquidazionePrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinLiquidazioneHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiaveResponse;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, liquidazione.
 * 
 * @author Valentina
 * @version 1.0.0 - 14/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneLiquidazioneInsPrimaNotaIntegrataBaseAction<M extends GestioneLiquidazionePrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaPrimaNotaIntegrataBaseAction<Liquidazione, ConsultaRegistrazioneMovFinLiquidazioneHelper, M> {


	/** Per la serializzazione */
	private static final long serialVersionUID = -253301131573003542L;

	
	/** Nome del model del completamento e validazione della reg con nuova primanota x la sessione */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_LIQUIDAZIONE_GSA = "CompletaLiquidazioneInsPrimaNotaIntegrataBaseGSAModel";
	
	/** Nome del model del completamento e validazione della reg con nuova primanota x la sessione */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_LIQUIDAZIONE_FIN = "CompletaLiquidazioneInsPrimaNotaIntegrataBaseFINModel";

	/** Nome del model del completamento della reg con nuova primanota per la sessione */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_LIQUIDAZIONE_GSA = "CompletaValidaLiquidazioneInsPrimaNotaIntegrataBaseGSAModel";
	
	/** Nome del model del completamento della reg con nuova primanota per la sessione */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_LIQUIDAZIONE_FIN = "CompletaValidaLiquidazioneInsPrimaNotaIntegrataBaseFINModel";
	
	@Autowired private transient LiquidazioneService liquidazioneService;
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName = "execute";
		
		checkCasoDUsoApplicabile("");
		//recuperoREgDaSessione DEVE ESSERCI
		RegistrazioneMovFin registrazioneMovFin = sessionHandler.getParametro(BilSessionParameter.REGISTRAZIONEMOVFIN);
		model.setRegistrazioneMovFin(registrazioneMovFin);
		log.debug(methodName, "registrazione ottenuta");
		
		if(inEsecuzioneRegistrazioneMovFin()) {
			return INPUT;
		}
		
		sessionHandler.setParametro(BilSessionParameter.LIQUIDAZIONE, null);
		Liquidazione liquidazione = (Liquidazione)model.getRegistrazioneMovFin().getMovimento();
		model.setLiquidazione(liquidazione);
		
		if (model.getPrimaNota()==null){
			model.setPrimaNota(new PrimaNota());
			model.impostaDatiNelModel();
		}
		computaStringheDescrizioneDaRegistrazione(model.getRegistrazioneMovFin());
		ricavaSoggettoDaMovimento(model.getRegistrazioneMovFin());
		
		try{
			caricaListaCausaleEP(false);
			caricaListaClassi();
			caricaListaTitoli();
		}catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			// Metto in sessione gli errori ed esco
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		getDatiDaLiquidazione();
		completaDescrizionePrimaNota(model.getLiquidazione());
		
		return SUCCESS;
		
	}
	
	/**
	 * Completamento della descrizione
	 * @param liquidazione la liquidazione
	 */
	private void completaDescrizionePrimaNota(Liquidazione liquidazione) {
		SubdocumentoSpesa subdocumentoSpesa = liquidazione.getSubdocumentoSpesa();
		
		if (subdocumentoSpesa != null) {
			DocumentoSpesa documentoSpesa = subdocumentoSpesa.getDocumento();
			
			if (documentoSpesa != null && documentoSpesa.getTipoDocumento().isAllegatoAtto()) {
				model.getPrimaNota().setDescrizione(String.format("%s / %s", model.getPrimaNota().getDescrizione(), subdocumentoSpesa.getCausaleOrdinativo()));
			}
		}
	}

	/**
	 * Ottenimento dei dati della liquidazione
	 */
	private void getDatiDaLiquidazione(){
		RicercaLiquidazionePerChiave req = model.creaRequestRicercaLiquidazionePerChiave();
		logServiceRequest(req);
		RicercaLiquidazionePerChiaveResponse res = liquidazioneService.ricercaLiquidazionePerChiave(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			return;
		}
		
		Liquidazione liquidazioneServizio = res.getLiquidazione();
		sessionHandler.setParametro(BilSessionParameter.LIQUIDAZIONE, liquidazioneServizio);
		
		model.setLiquidazione(liquidazioneServizio);
		model.setClassificatoreGerarchico(model.getLiquidazione().getCapitoloUscitaGestione().getMacroaggregato());
		
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinLiquidazioneHelper(liquidazioneServizio, model.isGestioneUEB()));
	}
	
	private void ricavaSoggettoDaMovimento(
			RegistrazioneMovFin registrazioneMovFin) {
		Liquidazione movim = (Liquidazione) registrazioneMovFin.getMovimento();
		if (movim!=null) {
			model.setSoggettoMovimentoFIN(movim.getSoggettoLiquidazione());
		}
		
	}
	
	
	@Override
	protected void computaStringheDescrizioneDaRegistrazione(RegistrazioneMovFin registrazioneMovFin){
		
		StringBuilder movimento = new StringBuilder();
		StringBuilder descrizione = new StringBuilder();
		Liquidazione liquidazione = (Liquidazione) registrazioneMovFin.getMovimento();
		descrizione.append("Liq ");
		descrizione.append(liquidazione.getAnnoLiquidazione() + "/" + liquidazione.getNumeroLiquidazione() + " ");
		descrizione.append(liquidazione.getDescrizioneLiquidazione());
		movimento.append(liquidazione.getAnnoLiquidazione() + "/" + liquidazione.getNumeroLiquidazione());
		movimento.append( " (liq)");

		model.setDescrMovimentoFinanziario(movimento.toString());
		model.getPrimaNota().setDescrizione(descrizione.toString());
		model.getPrimaNota().setDataRegistrazione(new Date());
	}

	/**
	 * Validazione per il metodo {@link #completeSalva()}.
	 */
	public void validateCompleteSalva() {
		// Se non ho la prima nota, qualcosa e' andato storto: esco subito
		checkNotNull(model.getPrimaNota(), "Prima Nota Integrata ", true);
		checkNotNullNorInvalidUid(model.getCausaleEP(), "CausaleEP");
		checkNotNull(model.getPrimaNota().getDataRegistrazione(), "Data registrazione ");
		checkNotNullNorEmpty(model.getPrimaNota().getDescrizione(), "Descrizione ");

		
		List<MovimentoDettaglio> listaMovimentiDettaglioFinal = checkScrittureCorrette();
		
		// Esco se ho errori
		if(hasErrori()) {
			return;
		}
		
		MovimentoEP movEP = new MovimentoEP();
		movEP.setCausaleEP(model.getCausaleEP());
		movEP.setRegistrazioneMovFin(model.getRegistrazioneMovFin());
		model.setListaMovimentoDettaglio(listaMovimentiDettaglioFinal);
		movEP.setListaMovimentoDettaglio(listaMovimentiDettaglioFinal);
		
		model.getListaMovimentoEP().add(movEP);
	}

	/**
	 * Completamento salva
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	
	public String completeSalva() {
		final String methodName = "completeSalva";
		
		aggiornaNumeroRiga();
		//l'inserisci è sempre da registrazione quindichiamo ilcompletaregistrazione
		
		// Inserimento della causale
		RegistraPrimaNotaIntegrata req = model.creaRequestRegistraPrimaNotaIntegrata();
		logServiceRequest(req);
		RegistraPrimaNotaIntegrataResponse res = primaNotaService.registraPrimaNotaIntegrata(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'inserimento della Prima Nota integrata");
			addErrori(res);
			return INPUT;
		}
		log.debug(methodName, "registrata correttamente Prima Nota integrata con uid " + res.getPrimaNota().getUid());
		if (model.isValidazione()) {
			log.debug(methodName, "validata correttamente Prima Nota integrata con uid " + res.getPrimaNota().getUid());
		}
		// Imposto i dati della causale restituitimi dal servizio
		model.setPrimaNota(res.getPrimaNota());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	

	@Override
	protected ElementoPianoDeiConti ottieniElementoPianoDeiContiDaMovimento() {
		return model.getLiquidazione() != null
			&& model.getLiquidazione().getCapitoloUscitaGestione() != null
				? model.getLiquidazione().getCapitoloUscitaGestione().getElementoPianoDeiConti()
				: null;
	}
	
	@Override
	protected Soggetto ottieniSoggettoDaMovimento() {
		return model.getLiquidazione() != null ? model.getLiquidazione().getSoggettoLiquidazione() : null;
	}
}

