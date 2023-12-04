/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedService;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedServiceExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.LeggiTipiCausaleSpesaExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.LeggiTipiCausaleSpesaKeyAdapter;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.ContoTesoreriaService;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesaPerElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesaPerElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoSpesaResponse;

/**
 * Cached version of {@link PreDocumentoSpesaService}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 *
 */
@Component
@CachedService
public class PreDocumentoSpesaServiceCachedImpl implements PreDocumentoSpesaService {

	@Autowired
	private transient PreDocumentoSpesaService preDocumentoSpesaService;
	@Autowired protected transient ContoTesoreriaService contoTesoreriaService;

	@Autowired
	private CachedServiceExecutor cachedServiceExecutor;

	@Override
	public InseriscePreDocumentoSpesaResponse inseriscePreDocumentoSpesa(InseriscePreDocumentoSpesa req) {
		return preDocumentoSpesaService.inseriscePreDocumentoSpesa(req);
	}

	@Override
	public ValidaStatoOperativoPreDocumentoSpesaResponse validaStatoOperativoPreDocumentoSpesa(ValidaStatoOperativoPreDocumentoSpesa req) {
		return preDocumentoSpesaService.validaStatoOperativoPreDocumentoSpesa(req);
	}

	@Override
	public RicercaPuntualePreDocumentoSpesaResponse ricercaPuntualePreDocumentoSpesa(RicercaPuntualePreDocumentoSpesa req) {
		return preDocumentoSpesaService.ricercaPuntualePreDocumentoSpesa(req);
	}

	@Override
	public RicercaSinteticaPreDocumentoSpesaResponse ricercaSinteticaPreDocumentoSpesa(RicercaSinteticaPreDocumentoSpesa req) {
		return preDocumentoSpesaService.ricercaSinteticaPreDocumentoSpesa(req);
	}

	@Override
	public RicercaDettaglioPreDocumentoSpesaResponse ricercaDettaglioPreDocumentoSpesa(RicercaDettaglioPreDocumentoSpesa req) {
		return preDocumentoSpesaService.ricercaDettaglioPreDocumentoSpesa(req);
	}

	@Override
	public AnnullaPreDocumentoSpesaResponse annullaPreDocumentoSpesa(AnnullaPreDocumentoSpesa req) {
		return preDocumentoSpesaService.annullaPreDocumentoSpesa(req);
	}

	@Override
	public void associaImputazioniContabiliPreDocumentoSpesa(AssociaImputazioniContabiliPreDocumentoSpesa req) {
		preDocumentoSpesaService.associaImputazioniContabiliPreDocumentoSpesa(req);
	}

	@Override
	public AggiornaPreDocumentoDiSpesaResponse aggiornaPreDocumentoDiSpesa(AggiornaPreDocumentoDiSpesa req) {
		return preDocumentoSpesaService.aggiornaPreDocumentoDiSpesa(req);
	}

	/**
	 * @deprecated utilizzare il metodo {@link #definiscePreDocumentoSpesa(DefiniscePreDocumentoSpesa)}
	 * o {@link #definiscePreDocumentoSpesaAsync(AsyncServiceRequestWrapper)}
	 */
	@Override
	@Deprecated
	public void definiscePreDocumentoDiSpesa(DefiniscePreDocumentoDiSpesa req) {
		preDocumentoSpesaService.definiscePreDocumentoDiSpesa(req);
	}

	@Override
	public InserisceCausaleSpesaResponse inserisceCausaleSpesa(InserisceCausaleSpesa req) {
		return preDocumentoSpesaService.inserisceCausaleSpesa(req);
	}

	@Override
	public AggiornaCausaleSpesaResponse aggiornaCausaleSpesa(AggiornaCausaleSpesa req) {
		return preDocumentoSpesaService.aggiornaCausaleSpesa(req);
	}

	@Override
	public AnnullaCausaleSpesaResponse annullaCausaleSpesa(AnnullaCausaleSpesa req) {
		return preDocumentoSpesaService.annullaCausaleSpesa(req);
	}

	@Override
	public RicercaDettaglioCausaleSpesaResponse ricercaDettaglioCausaleSpesa(RicercaDettaglioCausaleSpesa req) {
		return preDocumentoSpesaService.ricercaDettaglioCausaleSpesa(req);
	}

	@Override
	public RicercaSinteticaCausaleSpesaResponse ricercaSinteticaCausaleSpesa(RicercaSinteticaCausaleSpesa req) {
		return preDocumentoSpesaService.ricercaSinteticaCausaleSpesa(req);
	}

	@Override
	public LeggiTipiCausaleSpesaResponse leggiTipiCausaleSpesa(LeggiTipiCausaleSpesa req) {
		return cachedServiceExecutor.executeService(req, new LeggiTipiCausaleSpesaExecutor(preDocumentoSpesaService), new LeggiTipiCausaleSpesaKeyAdapter());
	}

	@Override
	public AggiornaStatoPreDocumentoDiSpesaResponse aggiornaStatoPreDocumentoDiSpesa(AggiornaStatoPreDocumentoDiSpesa req) {
		return preDocumentoSpesaService.aggiornaStatoPreDocumentoDiSpesa(req);
	}

	@Override
	public DettaglioStoricoCausaleSpesaResponse dettaglioStoricoCausaleSpesa(DettaglioStoricoCausaleSpesa req) {
		return preDocumentoSpesaService.dettaglioStoricoCausaleSpesa(req);
	}

	@Override
	public AssociaImputazioniContabiliVariatePreDocumentoSpesaResponse associaImputazioniContabiliVariatePreDocumentoSpesa(AssociaImputazioniContabiliVariatePreDocumentoSpesa parameters) {
		return preDocumentoSpesaService.associaImputazioniContabiliVariatePreDocumentoSpesa(parameters);
	}

	@Override
	public AsyncServiceResponse associaImputazioniContabiliVariatePreDocumentoSpesaAsync(AsyncServiceRequestWrapper<AssociaImputazioniContabiliVariatePreDocumentoSpesa> parameters) {
		return preDocumentoSpesaService.associaImputazioniContabiliVariatePreDocumentoSpesaAsync(parameters);
	}

	@Override
	public DefiniscePreDocumentoSpesaResponse definiscePreDocumentoSpesa(DefiniscePreDocumentoSpesa parameters) {
		return preDocumentoSpesaService.definiscePreDocumentoSpesa(parameters);
	}

	@Override
	public AsyncServiceResponse definiscePreDocumentoSpesaAsync(AsyncServiceRequestWrapper<DefiniscePreDocumentoSpesa> parameters) {
		return preDocumentoSpesaService.definiscePreDocumentoSpesaAsync(parameters);
	}

	@Override
	public DefiniscePreDocumentoSpesaPerElencoResponse definiscePreDocumentoSpesaPerElenco(DefiniscePreDocumentoSpesaPerElenco parameters) {
		return preDocumentoSpesaService.definiscePreDocumentoSpesaPerElenco(parameters);
	}

	@Override
	public AsyncServiceResponse definiscePreDocumentoSpesaPerElencoAsync(AsyncServiceRequestWrapper<DefiniscePreDocumentoSpesaPerElenco> parameters) {
		return preDocumentoSpesaService.definiscePreDocumentoSpesaPerElencoAsync(parameters);
	}

}
