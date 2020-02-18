/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedService;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedServiceExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.LeggiContiCorrenteExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.LeggiTipiCausaleEntrataExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.LeggiContiCorrenteKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.LeggiTipiCausaleEntrataKeyAdapter;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDataTrasmissionePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDataTrasmissionePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaDefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaDefiniscePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiCorrente;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiCorrenteResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStatoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoEntrataResponse;

/**
 * Cached version of {@link PreDocumentoEntrataService}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 *
 */
@Component
@CachedService
public class PreDocumentoEntrataServiceCachedImpl implements PreDocumentoEntrataService {

	@Autowired
	private transient PreDocumentoEntrataService preDocumentoEntrataService;

	@Autowired
	private CachedServiceExecutor cachedServiceExecutor;

	@Override
	public InseriscePreDocumentoEntrataResponse inseriscePreDocumentoEntrata(InseriscePreDocumentoEntrata req) {
		return preDocumentoEntrataService.inseriscePreDocumentoEntrata(req);
	}

	@Override
	public ValidaStatoOperativoPreDocumentoEntrataResponse validaStatoOperativoPreDocumentoEntrata(ValidaStatoOperativoPreDocumentoEntrata req) {
		return preDocumentoEntrataService.validaStatoOperativoPreDocumentoEntrata(req);
	}

	@Override
	public RicercaPuntualePreDocumentoEntrataResponse ricercaPuntualePreDocumentoEntrata(RicercaPuntualePreDocumentoEntrata req) {
		return preDocumentoEntrataService.ricercaPuntualePreDocumentoEntrata(req);
	}

	@Override
	public RicercaSinteticaPreDocumentoEntrataResponse ricercaSinteticaPreDocumentoEntrata(RicercaSinteticaPreDocumentoEntrata req) {
		return preDocumentoEntrataService.ricercaSinteticaPreDocumentoEntrata(req);
	}

	@Override
	public RicercaDettaglioPreDocumentoEntrataResponse ricercaDettaglioPreDocumentoEntrata(RicercaDettaglioPreDocumentoEntrata req) {
		return preDocumentoEntrataService.ricercaDettaglioPreDocumentoEntrata(req);
	}

	@Override
	public AnnullaPreDocumentoEntrataResponse annullaPreDocumentoEntrata(AnnullaPreDocumentoEntrata req) {
		return preDocumentoEntrataService.annullaPreDocumentoEntrata(req);
	}

	@Override
	public void associaImputazioniContabiliPreDocumentoEntrata(AssociaImputazioniContabiliPreDocumentoEntrata req) {
		preDocumentoEntrataService.associaImputazioniContabiliPreDocumentoEntrata(req);
	}

	@Override
	public AggiornaPreDocumentoDiEntrataResponse aggiornaPreDocumentoDiEntrata(AggiornaPreDocumentoDiEntrata req) {
		return preDocumentoEntrataService.aggiornaPreDocumentoDiEntrata(req);
	}

	/**
	 * @deprecated usare {@link #definiscePreDocumentoEntrata(DefiniscePreDocumentoEntrata)}
	 *             ovvero {@link #definiscePreDocumentoEntrataAsync(AsyncServiceRequestWrapper)}
	 */
	@Override
	@Deprecated
	public void definiscePreDocumentoDiEntrata(DefiniscePreDocumentoDiEntrata req) {
		preDocumentoEntrataService.definiscePreDocumentoDiEntrata(req);
	}

	@Override
	public InserisceCausaleEntrataResponse inserisceCausaleEntrata(InserisceCausaleEntrata req) {
		return preDocumentoEntrataService.inserisceCausaleEntrata(req);
	}

	@Override
	public AggiornaCausaleEntrataResponse aggiornaCausaleEntrata(AggiornaCausaleEntrata req) {
		return preDocumentoEntrataService.aggiornaCausaleEntrata(req);
	}

	@Override
	public AnnullaCausaleEntrataResponse annullaCausaleEntrata(AnnullaCausaleEntrata req) {
		return preDocumentoEntrataService.annullaCausaleEntrata(req);
	}

	@Override
	public RicercaDettaglioCausaleEntrataResponse ricercaDettaglioCausaleEntrata(RicercaDettaglioCausaleEntrata req) {
		return preDocumentoEntrataService.ricercaDettaglioCausaleEntrata(req);
	}

	@Override
	public RicercaSinteticaCausaleEntrataResponse ricercaSinteticaCausaleEntrata(RicercaSinteticaCausaleEntrata req) {
		return preDocumentoEntrataService.ricercaSinteticaCausaleEntrata(req);
	}

	@Override
	public LeggiTipiCausaleEntrataResponse leggiTipiCausaleEntrata(LeggiTipiCausaleEntrata req) {
		return cachedServiceExecutor.executeService(req, new LeggiTipiCausaleEntrataExecutor(preDocumentoEntrataService), new LeggiTipiCausaleEntrataKeyAdapter());
	}

	@Override
	public LeggiContiCorrenteResponse leggiContiCorrente(LeggiContiCorrente req) {
		return cachedServiceExecutor.executeService(req, new LeggiContiCorrenteExecutor(preDocumentoEntrataService), new LeggiContiCorrenteKeyAdapter());
	}
	
	@Override
	public AggiornaStatoPreDocumentoDiEntrataResponse aggiornaStatoPreDocumentoDiEntrata(AggiornaStatoPreDocumentoDiEntrata req) {
		return preDocumentoEntrataService.aggiornaStatoPreDocumentoDiEntrata(req);
	}

	@Override
	public DettaglioStoricoCausaleEntrataResponse dettaglioStoricoCausaleEntrata(DettaglioStoricoCausaleEntrata req) {
		return preDocumentoEntrataService.dettaglioStoricoCausaleEntrata(req);
	}

	@Override
	public AssociaImputazioniContabiliVariatePreDocumentoEntrataResponse associaImputazioniContabiliVariatePreDocumentoEntrata(AssociaImputazioniContabiliVariatePreDocumentoEntrata parameters) {
		return preDocumentoEntrataService.associaImputazioniContabiliVariatePreDocumentoEntrata(parameters);
	}

	@Override
	public AsyncServiceResponse associaImputazioniContabiliVariatePreDocumentoEntrataAsync(AsyncServiceRequestWrapper<AssociaImputazioniContabiliVariatePreDocumentoEntrata> parameters) {
		return preDocumentoEntrataService.associaImputazioniContabiliVariatePreDocumentoEntrataAsync(parameters);
	}

	@Override
	public AggiornaDataTrasmissionePreDocumentoEntrataResponse aggiornaDataTrasmissionePreDocumentoEntrata(AggiornaDataTrasmissionePreDocumentoEntrata parameters) {
		return preDocumentoEntrataService.aggiornaDataTrasmissionePreDocumentoEntrata(parameters);
	}

	@Override
	public AsyncServiceResponse aggiornaDataTrasmissionePreDocumentoEntrataAsync(AsyncServiceRequestWrapper<AggiornaDataTrasmissionePreDocumentoEntrata> parameters) {
		return preDocumentoEntrataService.aggiornaDataTrasmissionePreDocumentoEntrataAsync(parameters);
	}

	@Override
	public DefiniscePreDocumentoEntrataResponse definiscePreDocumentoEntrata(DefiniscePreDocumentoEntrata parameters) {
		return preDocumentoEntrataService.definiscePreDocumentoEntrata(parameters);
	}

	@Override
	public AsyncServiceResponse definiscePreDocumentoEntrataAsync(AsyncServiceRequestWrapper<DefiniscePreDocumentoEntrata> parameters) {
		return preDocumentoEntrataService.definiscePreDocumentoEntrataAsync(parameters);
	}

	@Override
	public CompletaDefiniscePreDocumentoEntrataResponse completaDefiniscePreDocumentoEntrata(CompletaDefiniscePreDocumentoEntrata parameters) {
		return preDocumentoEntrataService.completaDefiniscePreDocumentoEntrata(parameters);
	}

	@Override
	public AsyncServiceResponse completaDefiniscePreDocumentoEntrataAsync(AsyncServiceRequestWrapper<CompletaDefiniscePreDocumentoEntrata> parameters) {
		return preDocumentoEntrataService.completaDefiniscePreDocumentoEntrataAsync(parameters);
	}

	@Override
	public RicercaTotaliPreDocumentoEntrataPerStatoResponse ricercaTotaliPreDocumentoEntrataPerStato(RicercaTotaliPreDocumentoEntrataPerStato parameters) {
		return preDocumentoEntrataService.ricercaTotaliPreDocumentoEntrataPerStato(parameters);
	}

}
