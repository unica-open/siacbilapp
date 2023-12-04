/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedService;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedServiceExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaTipiAmbitoExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaTipiAmbitoKeyAdapter;
import it.csi.siac.siacbilser.frontend.webservice.ProgettoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaEntrataResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaSpesaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDeltaTraCronoprogrammi;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDeltaTraCronoprogrammiResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoComplessivo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoComplessivoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoEntrataResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoSpesaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CambiaFlagUsatoPerFpvCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.CambiaFlagUsatoPerFpvCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRigaEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRigaEntrataResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRigaSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRigaSpesaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaEntrataResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaSpesaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.OttieniFondoPluriennaleVincolatoCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.OttieniFondoPluriennaleVincolatoCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.PreparazioneCronoprogrammaDiGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.PreparazioneCronoprogrammaDiGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RiattivaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RiattivaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbitoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.CalcoloProspettoRiassuntivoCronoprogrammaAggiorna;
import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.CalcoloProspettoRiassuntivoCronoprogrammaAggiornaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.CalcoloProspettoRiassuntivoCronoprogrammaConsulta;
import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.CalcoloProspettoRiassuntivoCronoprogrammaConsultaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProvvedimento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProvvedimentoResponse;

/**
 * Cached version of {@link ProgettoService}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 *
 */
@Component
@CachedService
public class ProgettoServiceCachedImpl implements ProgettoService {

	@Autowired
	private transient ProgettoService progettoService;

	@Autowired
	private CachedServiceExecutor cachedServiceExecutor;

	@Override 
	public InserisceAnagraficaProgettoResponse inserisceAnagraficaProgetto(InserisceAnagraficaProgetto parameters) {
		return progettoService.inserisceAnagraficaProgetto(parameters);
	}

	@Override
	public AggiornaAnagraficaProgettoResponse aggiornaAnagraficaProgetto(AggiornaAnagraficaProgetto parameters) {
		return progettoService.aggiornaAnagraficaProgetto(parameters);
	}

	@Override
	public RicercaPuntualeProgettoResponse ricercaPuntualeProgetto(RicercaPuntualeProgetto parameters) {
		return progettoService.ricercaPuntualeProgetto(parameters);
	}

	@Override
	public RicercaSinteticaProgettoResponse ricercaSinteticaProgetto(RicercaSinteticaProgetto parameters) {
		return progettoService.ricercaSinteticaProgetto(parameters);
	}

	@Override
	public RicercaDettaglioProgettoResponse ricercaDettaglioProgetto(RicercaDettaglioProgetto parameters) {
		return progettoService.ricercaDettaglioProgetto(parameters);
	}

	@Override
	public AnnullaProgettoResponse annullaProgetto(AnnullaProgetto parameters) {
		return progettoService.annullaProgetto(parameters);
	}

	@Override
	public RiattivaProgettoResponse riattivaProgetto(RiattivaProgetto parameters) {
		return progettoService.riattivaProgetto(parameters);
	}

	@Override
	public InserisceCronoprogrammaResponse inserisceCronoprogramma(InserisceCronoprogramma parameters) {
		return progettoService.inserisceCronoprogramma(parameters);
	}

	@Override
	public RicercaDeiCronoprogrammiCollegatiAlProgettoResponse ricercaDeiCronoprogrammiCollegatiAlProgetto(RicercaDeiCronoprogrammiCollegatiAlProgetto parameters) {
		return progettoService.ricercaDeiCronoprogrammiCollegatiAlProgetto(parameters);
	}

	@Override
	public RicercaCronoprogrammaResponse ricercaCronoprogramma(RicercaCronoprogramma parameters) {
		return progettoService.ricercaCronoprogramma(parameters);
	}

	@Override
	public RicercaDettaglioCronoprogrammaResponse ricercaDettaglioCronoprogramma(RicercaDettaglioCronoprogramma parameters) {
		return progettoService.ricercaDettaglioCronoprogramma(parameters);
	}

	@Override
	public AggiornaAnagraficaCronoprogrammaResponse aggiornaAnagraficaCronoprogramma(AggiornaAnagraficaCronoprogramma parameters) {
		return progettoService.aggiornaAnagraficaCronoprogramma(parameters);
	}

	@Override
	public AnnullaCronoprogrammaResponse annullaCronoprogramma(AnnullaCronoprogramma parameters) {
		return progettoService.annullaCronoprogramma(parameters);
	}

	@Override
	public AggiornaRigaEntrataResponse aggiornaRigaEntrata(AggiornaRigaEntrata parameters) {
		return progettoService.aggiornaRigaEntrata(parameters);
	}

	@Override
	public InserisceRigaEntrataResponse inserisceRigaEntrata(InserisceRigaEntrata parameters) {
		return progettoService.inserisceRigaEntrata(parameters);
	}

	@Override
	public CancellaRigaEntrataResponse cancellaRigaEntrata(CancellaRigaEntrata parameters) {
		return progettoService.cancellaRigaEntrata(parameters);
	}

	@Override
	public AggiornaRigaSpesaResponse aggiornaRigaSpesa(AggiornaRigaSpesa parameters) {
		return progettoService.aggiornaRigaSpesa(parameters);
	}

	@Override
	public InserisceRigaSpesaResponse inserisceRigaSpesa(InserisceRigaSpesa parameters) {
		return progettoService.inserisceRigaSpesa(parameters);
	}

	@Override
	public CancellaRigaSpesaResponse cancellaRigaSpesa(CancellaRigaSpesa parameters) {
		return progettoService.cancellaRigaSpesa(parameters);
	}

	@Override
	public CalcoloProspettoRiassuntivoCronoprogrammaAggiornaResponse calcoloProspettoRiassuntivoCronoprogrammaAggiorna(CalcoloProspettoRiassuntivoCronoprogrammaAggiorna parameters) {
		return progettoService.calcoloProspettoRiassuntivoCronoprogrammaAggiorna(parameters);
	}

	@Override
	public CalcoloProspettoRiassuntivoCronoprogrammaConsultaResponse calcoloProspettoRiassuntivoCronoprogrammaConsulta(CalcoloProspettoRiassuntivoCronoprogrammaConsulta parameters) {
		return progettoService.calcoloProspettoRiassuntivoCronoprogrammaConsulta(parameters);
	}

	@Override
	public CalcoloDeltaTraCronoprogrammiResponse calcoloDeltaTraCronoprogrammi(CalcoloDeltaTraCronoprogrammi parameters) {
		return progettoService.calcoloDeltaTraCronoprogrammi(parameters);
	}

	@Override
	public PreparazioneCronoprogrammaDiGestioneResponse preparazioneCronoprogrammaDiGestione(PreparazioneCronoprogrammaDiGestione parameters) {
		return progettoService.preparazioneCronoprogrammaDiGestione(parameters);
	}

	@Override
	public RicercaTipiAmbitoResponse ricercaTipiAmbito(RicercaTipiAmbito parameters) {
		return cachedServiceExecutor.executeService(parameters, new RicercaTipiAmbitoExecutor(progettoService), new RicercaTipiAmbitoKeyAdapter());
	}

	/**
	 * @deprecated utilizzare il metodo {@link #ottieniFondoPluriennaleVincolatoCronoprogramma(OttieniFondoPluriennaleVincolatoCronoprogramma)}
	 */
	@Override
	@Deprecated
	public it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoCronoprogrammaResponse calcoloFondoPluriennaleVincolatoCronoprogramma(
			it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoCronoprogramma parameters) {
		return progettoService.calcoloFondoPluriennaleVincolatoCronoprogramma(parameters);
	}

	@Override
	public CalcoloFondoPluriennaleVincolatoComplessivoResponse calcoloFondoPluriennaleVincolatoComplessivo(CalcoloFondoPluriennaleVincolatoComplessivo parameters) {
		return progettoService.calcoloFondoPluriennaleVincolatoComplessivo(parameters);
	}

	@Override
	public CalcoloFondoPluriennaleVincolatoEntrataResponse calcoloFondoPluriennaleVincolatoEntrata(CalcoloFondoPluriennaleVincolatoEntrata parameters) {
		return progettoService.calcoloFondoPluriennaleVincolatoEntrata(parameters);
	}

	@Override
	public CalcoloFondoPluriennaleVincolatoSpesaResponse calcoloFondoPluriennaleVincolatoSpesa(CalcoloFondoPluriennaleVincolatoSpesa parameters) {
		return progettoService.calcoloFondoPluriennaleVincolatoSpesa(parameters);
	}

	@Override
	public CambiaFlagUsatoPerFpvCronoprogrammaResponse cambiaFlagUsatoPerFpvCronoprogramma(CambiaFlagUsatoPerFpvCronoprogramma parameters) {
		return progettoService.cambiaFlagUsatoPerFpvCronoprogramma(parameters);
	}

	@Override
	public OttieniFondoPluriennaleVincolatoCronoprogrammaResponse ottieniFondoPluriennaleVincolatoCronoprogramma(OttieniFondoPluriennaleVincolatoCronoprogramma parameters) {
		return progettoService.ottieniFondoPluriennaleVincolatoCronoprogramma(parameters);
	}

	@Override
	public RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioResponse ricercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio(RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio parameters) {
		return progettoService.ricercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio(parameters);
	}

	@Override
	public RicercaDeiCronoprogrammiCollegatiAlProvvedimentoResponse ricercaDeiCronoprogrammiCollegatiAlProvvedimento(
		RicercaDeiCronoprogrammiCollegatiAlProvvedimento parameters) {
		return progettoService.ricercaDeiCronoprogrammiCollegatiAlProvvedimento(parameters);
	}
}
