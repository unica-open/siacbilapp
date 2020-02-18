/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.frontend.webservice.AttoDiLeggeService;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedService;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedServiceExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.ListeGestioneSoggettoExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.ListeGestioneSoggettoKeyAdapter;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaDatiDurcSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaDatiDurcSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLegameSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLegameSoggettiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoProvvisorio;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoProvvisorioResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiungiSoggettoAllaClassificazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiungiSoggettoAllaClassificazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaClasse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaClasseResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLegameSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLegameSoggettiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaModalitaPagamentoInModifica;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaModalitaPagamentoInModificaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaSedeInModifica;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaSedeInModificaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaSoggettoInModifica;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaSoggettoInModificaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.CancellaSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.CancellaSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceClasse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceClasseResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggettoProvvisorio;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggettoProvvisorioResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSoggettiDellaClasse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSoggettiDellaClasseResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ModificaClasse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ModificaClasseResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaBanca;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaBancaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSedeSecondariaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSedeSecondariaPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RimuoviSoggettoDaClassificazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.RimuoviSoggettoDaClassificazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.VerificaIban;
import it.csi.siac.siacfinser.frontend.webservice.msg.VerificaIbanResponse;

/**
 * Cached version of {@link AttoDiLeggeService}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/10/2014
 * 
 */
@Component
@CachedService
public class SoggettoServiceCachedImpl implements SoggettoService {

	@Autowired
	private transient SoggettoService soggettoService;

	@Autowired
	private CachedServiceExecutor cachedServiceExecutor;

	@Override
	public InserisceSoggettoResponse inserisceSoggetto(InserisceSoggetto req) {
		return soggettoService.inserisceSoggetto(req);
	}

	@Override
	public RicercaSoggettiResponse ricercaSoggetti(RicercaSoggetti req) {
		return soggettoService.ricercaSoggetti(req);
	}

	@Override
	public RicercaSoggettoPerChiaveResponse ricercaSoggettoPerChiave(RicercaSoggettoPerChiave req) {
		return soggettoService.ricercaSoggettoPerChiave(req);
	}

	@Override
	public RicercaSedeSecondariaPerChiaveResponse ricercaSedeSecondariaPerChiave(RicercaSedeSecondariaPerChiave req) {
		return soggettoService.ricercaSedeSecondariaPerChiave(req);
	}

	@Override
	public RicercaModalitaPagamentoPerChiaveResponse ricercaModalitaPagamentoPerChiave(RicercaModalitaPagamentoPerChiave req) {
		return soggettoService.ricercaModalitaPagamentoPerChiave(req);
	}

	@Override
	public CancellaSoggettoResponse cancellaSoggetto(CancellaSoggetto req) {
		return soggettoService.cancellaSoggetto(req);
	}

	@Override
	public AggiornaSoggettoResponse aggiornaSoggetto(AggiornaSoggetto req) {
		return soggettoService.aggiornaSoggetto(req);
	}

	@Override
	public InserisceSoggettoProvvisorioResponse inserisceSoggettoProvvisorio(InserisceSoggettoProvvisorio req) {
		return soggettoService.inserisceSoggettoProvvisorio(req);
	}

	@Override
	public AnnullaSoggettoInModificaResponse annullaSoggettoInModifica(AnnullaSoggettoInModifica req) {
		return soggettoService.annullaSoggettoInModifica(req);
	}

	@Override
	public AnnullaSedeInModificaResponse annullaSedeInModifica(AnnullaSedeInModifica req) {
		return soggettoService.annullaSedeInModifica(req);
	}

	@Override
	public AnnullaModalitaPagamentoInModificaResponse annullaModalitaPagamentoInModifica(AnnullaModalitaPagamentoInModifica req) {
		return soggettoService.annullaModalitaPagamentoInModifica(req);
	}

	@Override
	public AggiornaSoggettoProvvisorioResponse aggiornaSoggettoProvvisorio(AggiornaSoggettoProvvisorio req) {
		return soggettoService.aggiornaSoggettoProvvisorio(req);
	}

	@Override
	public AggiornaLegameSoggettiResponse aggiornaLegameSoggetti(AggiornaLegameSoggetti req) {
		return soggettoService.aggiornaLegameSoggetti(req);
	}

	@Override
	public AnnullaLegameSoggettiResponse annullaLegameSoggetti(AnnullaLegameSoggetti req) {
		return soggettoService.annullaLegameSoggetti(req);
	}

	@Override
	public ListeGestioneSoggettoResponse listeGestioneSoggetto(ListeGestioneSoggetto req) {
		return cachedServiceExecutor.executeService(req, new ListeGestioneSoggettoExecutor(soggettoService), new ListeGestioneSoggettoKeyAdapter());
	}

	@Override
	public RicercaBancaResponse ricercaBanca(RicercaBanca arg0) {
		return soggettoService.ricercaBanca(arg0);
	}

	@Override
	public VerificaIbanResponse verificaIban(VerificaIban arg0) {
		return soggettoService.verificaIban(arg0);
	}

	@Override
	public AggiungiSoggettoAllaClassificazioneResponse aggiungiSoggettoAllaClassificazione(AggiungiSoggettoAllaClassificazione arg0) {
		return soggettoService.aggiungiSoggettoAllaClassificazione(arg0);
	}

	@Override
	public RimuoviSoggettoDaClassificazioneResponse rimuoviSoggettoDaClassificazione(RimuoviSoggettoDaClassificazione arg0) {
		return soggettoService.rimuoviSoggettoDaClassificazione(arg0);
	}

	@Override
	public AnnullaClasseResponse annullaClasse(AnnullaClasse arg0) {
		return soggettoService.annullaClasse(arg0);
	}

	@Override
	public InserisceClasseResponse inserisceClasse(InserisceClasse arg0) {
		return soggettoService.inserisceClasse(arg0);
	}

	@Override
	public ModificaClasseResponse modificaClasse(ModificaClasse arg0) {
		return soggettoService.modificaClasse(arg0);
	}

	@Override
	public RicercaSoggettiOttimizzatoResponse ricercaSoggettiOttimizzato(RicercaSoggettiOttimizzato arg0) {
		return soggettoService.ricercaSoggettiOttimizzato(arg0);
	}

	@Override
	public ListaSoggettiDellaClasseResponse listaSoggettiDellaClasse(ListaSoggettiDellaClasse request) {
		return soggettoService.listaSoggettiDellaClasse(request);
	}

	@Override
	public AggiornaDatiDurcSoggettoResponse aggiornaDatiDurcSoggetto(AggiornaDatiDurcSoggetto request) {
		return soggettoService.aggiornaDatiDurcSoggetto(request);
	}

}
