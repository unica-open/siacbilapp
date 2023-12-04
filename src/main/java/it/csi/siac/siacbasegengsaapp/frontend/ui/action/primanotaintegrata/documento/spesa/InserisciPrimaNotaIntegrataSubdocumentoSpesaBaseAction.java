/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.documento.spesa;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.documento.InserisciPrimaNotaIntegrataSubdocumentoBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento.spesa.InserisciPrimaNotaIntegrataDocumentoSpesaBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaSpesaRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinDocumentoSpesaHelper;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per la validazione della prima integrata per il subdocumento di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/10/2015
 * @param <M> la tipizzazione del model
 *
 */
public abstract class InserisciPrimaNotaIntegrataSubdocumentoSpesaBaseAction<M extends InserisciPrimaNotaIntegrataDocumentoSpesaBaseModel>
		extends InserisciPrimaNotaIntegrataSubdocumentoBaseAction<DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa, ElementoQuotaSpesaRegistrazioneMovFin, ConsultaRegistrazioneMovFinDocumentoSpesaHelper, M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3296610811952891963L;
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	
	@Override
	protected void impostazioneDocumento() throws WebServiceInvocationFailureException {
		RegistrazioneMovFin registrazioneMovFin = model.getRegistrazioneMovFin();
		SubdocumentoSpesa subdocumentoSpesa = (SubdocumentoSpesa) registrazioneMovFin.getMovimento();
		
		if(subdocumentoSpesa == null || subdocumentoSpesa.getDocumento() == null) {
			throw new WebServiceInvocationFailureException("Errore nel caricamento del movimento: dati non reperiti correttamente");
		}
		
		model.setDocumento(subdocumentoSpesa.getDocumento());
	}
	
	@Override
	protected void ottieniDettaglioSubdocumento() throws WebServiceInvocationFailureException {
		
		// Non vorrei ci sia un problema con Struts2. Per sicurezza, ricasto io.
		if(!(model.getSubdocumento() instanceof SubdocumentoSpesa)) {
			SubdocumentoSpesa subdocumento = new SubdocumentoSpesa();
			subdocumento.setUid(model.getUidSubdocumento());
			model.setSubdocumento(subdocumento);
		}
		
		RicercaDettaglioQuotaSpesa request = model.creaRequestRicercaDettaglioQuotaSpesa();
		logServiceRequest(request);
		RicercaDettaglioQuotaSpesaResponse response = documentoSpesaService.ricercaDettaglioQuotaSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioQuotaSpesa.class, response));
		}
		if(response.getSubdocumentoSpesa() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Subdocumento", "uid " + model.getSubdocumento().getUid()));
			throw new WebServiceInvocationFailureException("Subdocumento con uid " + model.getSubdocumento().getUid() + " non presente");
		}
		SubdocumentoSpesa subdocumento = response.getSubdocumentoSpesa();
		
		if(subdocumento.getImpegno() == null) {
			String msg = "il subdocumento " + subdocumento.getNumero() + " risulta essere senza impegno associato";
			addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(msg));
			throw new WebServiceInvocationFailureException(msg);
		}
		
		model.setSubdocumento(subdocumento);
		model.setClassificatoreGerarchico(subdocumento.getImpegno() == null ? null : subdocumento.getImpegno().getCapitoloUscitaGestione().getMacroaggregato());
	}
	
	@Override
	protected ElementoPianoDeiConti ottieniElementoPianoDeiContiDaMovimento() {
		return model.getSubdocumento() != null
			&& model.getSubdocumento().getImpegno() != null
			&& model.getSubdocumento().getImpegno().getCapitoloUscitaGestione() != null
				? model.getSubdocumento().getImpegno().getCapitoloUscitaGestione().getElementoPianoDeiConti()
				: null;
	}
	
	@Override
	protected Soggetto ottieniSoggettoDaMovimento() {
		return model.getDocumento() != null ? model.getDocumento().getSoggetto() : null;
	}

	@Override
	protected void setImportoInElementoScrittura(boolean isNCD, BigDecimal imponibile, BigDecimal impostaDetraibile, ElementoScritturaPrimaNotaIntegrata elementoScrittura) {

		if ((elementoScrittura.isSegnoDare() && isNCD) || (elementoScrittura.isSegnoAvere() && !isNCD)) {
			// Il dare ha importo pari a imponibile + imposta // L'avere ha
			// importo pari a imponibile + imposta
			elementoScrittura.getMovimentoDettaglio().setImporto(imponibile.add(impostaDetraibile));
		} else {
			if (elementoScrittura.isTipoImportoImponibile()) {
				elementoScrittura.getMovimentoDettaglio().setImporto(imponibile);
			} else if (elementoScrittura.isTipoImportoImposta()) {
				elementoScrittura.getMovimentoDettaglio().setImporto(impostaDetraibile);
			} else if (elementoScrittura.isTipoImportoLordo()) {
				elementoScrittura.getMovimentoDettaglio().setImporto(imponibile.add(impostaDetraibile).setScale(FormatUtils.MATH_CONTEXT.getPrecision(), FormatUtils.MATH_CONTEXT.getRoundingMode()));
			}
		}
	}

}
