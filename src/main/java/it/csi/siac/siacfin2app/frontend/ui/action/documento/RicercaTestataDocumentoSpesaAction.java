/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.RicercaTestataDocumentoSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;

/**
 * Classe di Action per la gestione della ricerca della testata del Documento di spesa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 10/07/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaTestataDocumentoSpesaAction extends GenericDocumentoAction<RicercaTestataDocumentoSpesaModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -3058548466132311031L;
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();

		checkAndObtainListaTipoDocumento(TipoFamigliaDocumento.IVA_SPESA, null, null);
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

		RicercaSinteticaDocumentoSpesa request = model.creaRequestRicercaSinteticaDocumentoSpesa();
		logServiceRequest(request);
		RicercaSinteticaDocumentoSpesaResponse response = documentoSpesaService.ricercaSinteticaTestataDocumentoSpesa(request);
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
		
		log.debug(methodName, "Ricerca effettuata con successo. Totale risultati trovati: "+response.getDocumenti().getTotaleElementi());

		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_TESTATA_DOCUMENTI_SPESA, request);

		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_TESTATA_DOCUMENTI_SPESA, response.getDocumenti());

		log.debug(methodName, "Imposto la stringa da visualizzare nei risultati ricerca");
		List<TipoDocumento> listaTipoDocumento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO);
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile =
			sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);

		sessionHandler.setParametro(BilSessionParameter.RIEPILOGO_RICERCA_TESTATA_DOCUMENTO,
			model.componiStringaRiepilogo(model.getDocumento(), listaTipoDocumento, listaTipoAtto, listaStrutturaAmministrativoContabile));
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA, response.getImportoTotale());
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		return SUCCESS;
	}

	/**
	 * Validazione per il metodo {@link RicercaTestataDocumentoSpesaAction#ricercaDocumento()}.
	 */
	public void validateRicercaDocumento() {
		final String methodName = "validateRicercaDocumento";
		log.debugStart(methodName, "Verifica campi");
		
		DocumentoSpesa documento = model.getDocumento();
		boolean formValido = checkPresenzaIdEntita(documento.getTipoDocumento());
		
		if(formValido) {
			// Ho il tipo documento. Ne tiro su i dati
			TipoDocumento tipoDocumento = ComparatorUtils.searchByUid(model.getListaTipoDocumento(), documento.getTipoDocumento());
			documento.setTipoDocumento(tipoDocumento);
		}
		
		formValido = formValido ||
			checkCampoValorizzato(documento.getStatoOperativoDocumento(), "Stato") ||
			checkCampoValorizzato(documento.getAnno(), "Anno") ||
			checkStringaValorizzata(documento.getNumero(), "Numero") ||
			checkCampoValorizzato(documento.getDataEmissione(), "Data") ||
			checkStringaValorizzata(model.getFlagIva(), "Iva");
		model.setCampoFormPrincipalePresente(Boolean.valueOf(formValido));
		
		//verifica dati movimento
		
		
		// verifica soggetto
		boolean soggettoPresente = model.getSoggetto() != null && StringUtils.isNotBlank(model.getSoggetto().getCodiceSoggetto());
		model.setSoggettoPresente(Boolean.valueOf(soggettoPresente));
		
		//verifica provvedimento
		boolean provvedimentoPresente = checkProvvedimentoEsistente();
		model.setProvvedimentoPresente(Boolean.valueOf(provvedimentoPresente));
		
		// Controllo di aver impostato almeno un criterio di ricerca
		checkCondition(formValido || soggettoPresente || provvedimentoPresente, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		
		// Check sul soggetto
		if(soggettoPresente) {
			validaSoggetto(false);
		}
		//Check sul provvedimento: se è stato valorizzato deve esistere e i campi indicati devono restituire un'unico provvedimento
		if(provvedimentoPresente && checkFormProvvedimentoValido(model.getAttoAmministrativo())) {
			verificaUnicitaProvvedimento();
		}
		
		log.debugEnd(methodName, "");
	}
	
}
