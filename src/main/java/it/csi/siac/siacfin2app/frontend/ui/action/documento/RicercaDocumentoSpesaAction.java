/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.RicercaDocumentoSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;

/**
 * Classe di Action per la gestione della ricerca del Documento.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 04/02/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaDocumentoSpesaAction extends GenericDocumentoAction<RicercaDocumentoSpesaModel> {

	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;

	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	

	@Override
	public void prepare() throws Exception {
		super.prepare();

		checkAndObtainListaTipoDocumento(TipoFamigliaDocumento.SPESA, null, null);
		checkAndObtainListaClassiSoggetto();
		checkAndObtainListaTipiAtto();
		caricaListaStati();
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return SUCCESS;
	}

	/**
	 * Ricerca dei documenti sulla base dei criteri impostati.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaDocumento() {
		final String methodName = "ricercaDocumento";

		if (!checkIsValidaRicercaDocumento()) {
			log.debug(methodName, "Validazione fallita");
			return INPUT;
		}

		log.debug(methodName, "Effettua la ricerca");

		RicercaSinteticaModulareDocumentoSpesa request = model.creaRequestRicercaSinteticaModulareDocumentoSpesa();
		logServiceRequest(request);

		RicercaSinteticaModulareDocumentoSpesaResponse response = documentoSpesaService.ricercaSinteticaModulareDocumentoSpesa(request);
		logServiceResponse(response);

		
		if (response.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(response);
			return INPUT;
		}
		
		
		
		if(response.getDocumenti().getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		log.debug(methodName, "Totale: "+response.getDocumenti().getTotaleElementi());
		 
		
		log.debug(methodName, "Ricerca effettuata con successo");

		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_DOCUMENTI_SPESA, request);

		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_DOCUMENTI_SPESA, response.getDocumenti());

		log.debug(methodName, "Imposto la stringa da visualizzare nei risultati ricerca");
		List<TipoDocumento> listaTipoDocumento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO);
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);

		sessionHandler.setParametro(BilSessionParameter.RIEPILOGO_RICERCA_DOCUMENTO,
				model.componiStringaRiepilogo(model.getDocumento(), listaTipoDocumento, listaTipoAtto, listaStrutturaAmministrativoContabile));
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA, response.getImportoTotale());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		return SUCCESS;
	}

	/**
	 * Validazione per il metodo {@link RicercaDocumentoSpesaAction#ricercaDocumento()}.
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
	 */
	public boolean checkIsValidaRicercaDocumento() {
		final String methodName = "checkIsValidaRicercaDocumento";
		log.debugStart(methodName, "Verifica campi");
		
		DocumentoSpesa documento = model.getDocumento();
		boolean formValido = checkPresenzaIdEntita(documento.getTipoDocumento());
		
		if(formValido) {
			// Ho il tipo documento. Ne tiro su i dati
			TipoDocumento tipoDocumento = ComparatorUtils.searchByUid(model.getListaTipoDocumento(), documento.getTipoDocumento());
			documento.setTipoDocumento(tipoDocumento);
		}
		
		formValido = formValido
				|| checkCampoValorizzato(documento.getStatoOperativoDocumento(), "Stato")
				|| checkCampoValorizzato(documento.getAnno(), "Anno")
				|| checkStringaValorizzata(documento.getNumero(), "Numero")
				|| checkCampoValorizzato(documento.getDataEmissione(), "Data")
				|| checkStringaValorizzata(model.getFlagIva(), "Iva")
				|| checkStringaValorizzata(model.getFlagCollegatoCEC(), "Collegato CEC")
				|| checkStringaValorizzata(model.getFlagAttivaScrittureContabili(), "Attiva Scritture Contabili")
				|| model.areDatiProtocolloPresenti(documento);
		
		model.setCampoFormPrincipalePresente(formValido);
		
		//verifica dati movimento
		boolean movimentoPresente = model.getImpegno().getAnnoMovimento() != 0 || model.getImpegno().getNumero() != null;
		model.setMovimentoPresente(movimentoPresente);
		
		log.debug(methodName, "movimento presente " + movimentoPresente + " - " + model.getImpegno().getAnnoMovimento() + " - " + model.getImpegno().getNumero());
		
		// verifica soggetto
		boolean soggettoPresente = model.getSoggetto().getCodiceSoggetto()!= null && !(model.getSoggetto().getCodiceSoggetto().length()==0);
		model.setSoggettoPresente(soggettoPresente);
		log.debug(methodName, "Soggetto presente? " + soggettoPresente);
		
		//verifica provvedimento
		boolean provvedimentoPresente = checkProvvedimentoEsistente();
		
		model.setProvvedimentoPresente(provvedimentoPresente);
		log.debug(methodName, "Provvedimento presente? " + provvedimentoPresente);
		
		boolean elencoPresente = model.getElencoDocumenti() != null && ( model.getElencoDocumenti().getAnno() != null || model.getElencoDocumenti().getNumero() != null);
		if(elencoPresente){
			checkCondition(model.getElencoDocumenti().getAnno() != null && model.getElencoDocumenti().getNumero() != null, ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno e Numero Elenco", ": devono essere entrambi valorizzati o non valorizzati"));
		}
		model.setElencoPresente(elencoPresente);
		
		
		checkCondition(formValido || soggettoPresente || provvedimentoPresente || movimentoPresente || elencoPresente, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		
		// Check sul soggetto
		if(soggettoPresente) {
			validaSoggetto(false);
		}
				
		//Check sul provvedimento: se è stato valorizzato deve esistere e i campi indicati devono restituire un'unico provvedimento
		if(provvedimentoPresente && checkFormProvvedimentoValido(model.getAttoAmministrativo())) {
			log.info(methodName, "setto attoamm fine");
			//verificaUnicitaProvvedimento();
			//siac-5660
			AttoAmministrativo attoAmm = new AttoAmministrativo();
			attoAmm.setUid(model.getUidProvvedimento()!=null ? model.getUidProvvedimento() :0);
			attoAmm.setAnno(model.getAttoAmministrativo().getAnno() );
			attoAmm.setNumero(model.getAttoAmministrativo().getNumero() );
			attoAmm.setTipoAtto(model.getTipoAtto());
			attoAmm.setStrutturaAmmContabile(model.getStrutturaAmministrativoContabile());		
			model.setAttoAmministrativo(attoAmm);
			log.info(methodName, "setto attoamm fine ");

		}
		
		// Anno Impegno e numero devono essere entrambi presenti o entrambi assenti
		if(movimentoPresente) {
			if(((model.getImpegno().getAnnoMovimento() != 0 && Integer.toString(model.getImpegno().getAnnoMovimento()) != null) && model.getImpegno().getNumero() == null) ||
					((model.getImpegno().getAnnoMovimento() == 0 || Integer.toString(model.getImpegno().getAnnoMovimento()) == null) && model.getImpegno().getNumero() != null)) {
				checkCondition(false, ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno e Numero Movimento", ": devono essere entrambi valorizzati o non valorizzati"));
			} else {
				// sono entrambi valorizzati, verifico che esista l'impegno
				verificaUnicitaImpegno();
			}
		}
		log.debugEnd(methodName, "");
		return !hasErrori();
	}

	/**
	 * Validazione di unicit&agrave; per l'impegno.
	 */
	protected void verificaUnicitaImpegno() {
		
		RicercaImpegnoPerChiaveOttimizzato request = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
		RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
		} else {
			checkCondition(response.getImpegno() != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Movimento Anno e numero","L'impegno indicato"));
			model.setImpegno(response.getImpegno());
		}
	}
	
}
