/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscgest;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloUscita;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.DisponibilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.RisorsaAccantonata;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubImpegni;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpSub;

/**
 * Classe di model per il Capitolo di Uscita Gestione. Contiene una mappatura
 * dei form per l'aggiornamento del Capitolo di Uscita Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 30/07/2013
 *
 */
public class AggiornaCapitoloUscitaGestioneModel extends CapitoloUscitaModel {

	/** Per las serializazione */
	private static final long serialVersionUID = -709373074631819565L;

	/* Terza maschera: importi */
	private ImportiCapitoloUG importiCapitoloUscitaGestioneM1;
	private ImportiCapitoloUG importiCapitoloUscitaGestione0;
	private ImportiCapitoloUG importiCapitoloUscitaGestione1;
	private ImportiCapitoloUG importiCapitoloUscitaGestione2;

	/* Seconda maschera: altri dati */
	private CapitoloUscitaGestione capitoloUscitaGestione;

	// GESC0014
	private List<ImportiCapitoloPerComponente> importiComponentiCapitolo = new ArrayList<ImportiCapitoloPerComponente>();

	private ImportiCapitolo importiResidui;
	private ImportiCapitolo importiAnniSucc;

	// 6881 NUOVI DATI
	private ImportiCapitoloPerComponente competenzaStanziamento;
	private ImportiCapitoloPerComponente competenzaImpegnato;
	private ImportiCapitoloPerComponente disponibilita;
	private ImportiCapitoloPerComponente residuiEffettivi;
	private ImportiCapitoloPerComponente residuiPresunti;
	private ImportiCapitoloPerComponente cassaStanziato;
	private ImportiCapitoloPerComponente cassaPagato;
	
	
	//fix jira 7122
	
	private DisponibilitaCapitoloUscitaGestione disponibilitaCapitoloUscitaGestioneAnno0;
	private DisponibilitaCapitoloUscitaGestione disponibilitaCapitoloUscitaGestioneAnno1;
	private DisponibilitaCapitoloUscitaGestione disponibilitaCapitoloUscitaGestioneAnno2;
	private DisponibilitaCapitoloUscitaGestione disponibilitaCapitoloUscitaGestioneResiduo;
	
	//SIAC-6884 Per Discriminare le componenti di default
	private boolean capitoloFondino = false;
	private boolean stanziamentiNegativiPresenti;
	private boolean componentiDiversiDaFresco;

	

	/** Costruttore vuoto di default */
	public AggiornaCapitoloUscitaGestioneModel() {
		super();
		setTitolo("Aggiornamento Capitolo Spesa Gestione");
	}

	/**
	 * @return the importiCapitoloUscitaGestioneM1
	 */
	public ImportiCapitoloUG getImportiCapitoloUscitaGestioneM1() {
		return importiCapitoloUscitaGestioneM1;
	}

	/**
	 * @param importiCapitoloUscitaGestioneM1
	 *            the importiCapitoloUscitaGestioneM1 to set
	 */
	public void setImportiCapitoloUscitaGestioneM1(ImportiCapitoloUG importiCapitoloUscitaGestioneM1) {
		this.importiCapitoloUscitaGestioneM1 = importiCapitoloUscitaGestioneM1;
	}

	/**
	 * @return the importiCapitoloUscitaGestione0
	 */
	public ImportiCapitoloUG getImportiCapitoloUscitaGestione0() {
		return importiCapitoloUscitaGestione0;
	}

	/**
	 * @param importiCapitoloUscitaGestione0
	 *            the importiCapitoloUscitaGestione0 to set
	 */
	public void setImportiCapitoloUscitaGestione0(ImportiCapitoloUG importiCapitoloUscitaGestione0) {
		this.importiCapitoloUscitaGestione0 = importiCapitoloUscitaGestione0;
	}

	/**
	 * @return the importiCapitoloUscitaGestione1
	 */
	public ImportiCapitoloUG getImportiCapitoloUscitaGestione1() {
		return importiCapitoloUscitaGestione1;
	}

	/**
	 * @param importiCapitoloUscitaGestione1
	 *            the importiCapitoloUscitaGestione1 to set
	 */
	public void setImportiCapitoloUscitaGestione1(ImportiCapitoloUG importiCapitoloUscitaGestione1) {
		this.importiCapitoloUscitaGestione1 = importiCapitoloUscitaGestione1;
	}

	/**
	 * @return the importiCapitoloUscitaGestione2
	 */
	public ImportiCapitoloUG getImportiCapitoloUscitaGestione2() {
		return importiCapitoloUscitaGestione2;
	}

	/**
	 * @param importiCapitoloUscitaGestione2
	 *            the importiCapitoloUscitaGestione2 to set
	 */
	public void setImportiCapitoloUscitaGestione2(ImportiCapitoloUG importiCapitoloUscitaGestione2) {
		this.importiCapitoloUscitaGestione2 = importiCapitoloUscitaGestione2;
	}

	/**
	 * @return the capitoloUscitaGestione
	 */
	public CapitoloUscitaGestione getCapitoloUscitaGestione() {
		return capitoloUscitaGestione;
	}

	/**
	 * @param capitoloUscitaGestione
	 *            the capitoloUscitaGestione to set
	 */
	public void setCapitoloUscitaGestione(CapitoloUscitaGestione capitoloUscitaGestione) {
		this.capitoloUscitaGestione = capitoloUscitaGestione;
	}

	/**
	 * Ottiene l'uid del capitolo da aggiornare.
	 * 
	 * @return l'uid del Capitolo di Uscita Previsione da aggiornare
	 */
	public int getUidDaAggiornare() {
		return capitoloUscitaGestione == null ? 0 : capitoloUscitaGestione.getUid();
	}

	/**
	 * Imposta l'uid nel Capitolo di Uscita Previsione da Aggiornare.
	 * 
	 * @param uidDaAggiornare
	 *            l'uid da impostare
	 */
	public void setUidDaAggiornare(int uidDaAggiornare) {
		if (capitoloUscitaGestione == null) {
			// Non dovrebbe mai accadere tale eventualità
			capitoloUscitaGestione = new CapitoloUscitaGestione();
		}
		capitoloUscitaGestione.setUid(uidDaAggiornare);
	}

	
	//SIAC-6884
	/**
	 * @return the capitoloFondino
	 */
	public boolean isCapitoloFondino() {
		return capitoloFondino;
	}

	/**
	 * @param capitoloFondino the capitoloFondino to set
	 */
	public void setCapitoloFondino(boolean capitoloFondino) {
		this.capitoloFondino = capitoloFondino;
	}

	/**
	 * @return the stanziamentiNegativiPresenti
	 */
	public boolean getStanziamentiNegativiPresenti() {
		return stanziamentiNegativiPresenti;
	}

	/**
	 * @param stanziamentiNegativiPresenti the stanziamentiNegativiPresenti to set
	 */
	public void setStanziamentiNegativiPresenti(boolean stanziamentiNegativiPresenti) {
		this.stanziamentiNegativiPresenti = stanziamentiNegativiPresenti;
	}

	/**
	 * @param importiComponentiCapitoloUscitaGestione
	 *            the importiComponentiCapitoloUscitaGestione to set
	 */
	public void setImportiComponentiCapitolo(List<ImportiCapitoloPerComponente> importiComponenti) {
		this.importiComponentiCapitolo = importiComponenti;
	}

	public List<ImportiCapitoloPerComponente> getImportiComponentiCapitolo() {
		return importiComponentiCapitolo;
	}

	/* Requests */
	
	// FIXME da eliminare dopo 

	public ImportiCapitolo getImportiResidui() {
		return importiResidui;
	}

	public void setImportiResidui(ImportiCapitolo importiResidui) {
		this.importiResidui = importiResidui;
	}

	public ImportiCapitolo getImportiAnniSucc() {
		return importiAnniSucc;
	}

	public void setImportiAnniSucc(ImportiCapitolo importiAnniSucc) {
		this.importiAnniSucc = importiAnniSucc;
	}

	public ImportiCapitoloPerComponente getCompetenzaStanziamento() {
		return competenzaStanziamento;
	}

	public void setCompetenzaStanziamento(ImportiCapitoloPerComponente competenzaStanziamento) {
		this.competenzaStanziamento = competenzaStanziamento;
	}

	public ImportiCapitoloPerComponente getCompetenzaImpegnato() {
		return competenzaImpegnato;
	}

	public void setCompetenzaImpegnato(ImportiCapitoloPerComponente competenzaImpegnato) {
		this.competenzaImpegnato = competenzaImpegnato;
	}

	public ImportiCapitoloPerComponente getDisponibilita() {
		return disponibilita;
	}

	public void setDisponibilita(ImportiCapitoloPerComponente disponibilita) {
		this.disponibilita = disponibilita;
	}

	public ImportiCapitoloPerComponente getResiduiEffettivi() {
		return residuiEffettivi;
	}

	public void setResiduiEffettivi(ImportiCapitoloPerComponente residuiEffettivi) {
		this.residuiEffettivi = residuiEffettivi;
	}

	public ImportiCapitoloPerComponente getResiduiPresunti() {
		return residuiPresunti;
	}

	public void setResiduiPresunti(ImportiCapitoloPerComponente residuiPresunti) {
		this.residuiPresunti = residuiPresunti;
	}

	public ImportiCapitoloPerComponente getCassaStanziato() {
		return cassaStanziato;
	}

	public void setCassaStanziato(ImportiCapitoloPerComponente cassaStanziato) {
		this.cassaStanziato = cassaStanziato;
	}

	public ImportiCapitoloPerComponente getCassaPagato() {
		return cassaPagato;
	}

	public void setCassaPagato(ImportiCapitoloPerComponente cassaPagato) {
		this.cassaPagato = cassaPagato;
	}

	/**
	 * Restituisce una Request di tipo {@link AggiornaCapitoloDiUscitaGestione}
	 * a partire dal Model.
	 * 
	 * @param classificatoreAggiornamento
	 *            i classificatori salvati in sessione
	 * 
	 * @return la Request creata
	 */
	public AggiornaCapitoloDiUscitaGestione creaRequestAggiornaCapitoloDiUscitaGestione(
			ClassificatoreAggiornamentoCapitoloUscita classificatoreAggiornamento) {
		AggiornaCapitoloDiUscitaGestione request = creaRequest(AggiornaCapitoloDiUscitaGestione.class);

		capitoloUscitaGestione.setAnnoCapitolo(getBilancio().getAnno());

		// Modularizzazione dei campi
		Missione missioneDaInjettare = valutaInserimento(getMissione(), classificatoreAggiornamento.getMissione(),
				isMissioneEditabile());
		Programma programmaDaInjettare = valutaInserimento(getProgramma(), classificatoreAggiornamento.getProgramma(),
				isProgrammaEditabile());
		TitoloSpesa titoloSpesaDaInjettare = valutaInserimento(getTitoloSpesa(),
				classificatoreAggiornamento.getTitoloSpesa(), isTitoloSpesaEditabile());
		Macroaggregato macroaggregatoDaInjettare = valutaInserimento(getMacroaggregato(),
				classificatoreAggiornamento.getMacroaggregato(), isMacroaggregatoEditabile());
		ElementoPianoDeiConti elementoPianoDeiContiDaInjettare = valutaInserimento(getElementoPianoDeiConti(),
				classificatoreAggiornamento.getElementoPianoDeiConti(), isElementoPianoDeiContiEditabile());
		StrutturaAmministrativoContabile strutturaAmministrativoContabileDaInjettare = valutaInserimento(
				getStrutturaAmministrativoContabile(),
				classificatoreAggiornamento.getStrutturaAmministrativoContabile(),
				isStrutturaAmministrativoContabileEditabile());
		// Controllo che i classificatori siano cambiati
		ClassificazioneCofog classificazioneCofogDaInjettare = valutaInserimento(getClassificazioneCofog(),
				classificatoreAggiornamento.getClassificazioneCofog(), isClassificazioneCofogEditabile());
		TipoFinanziamento tipoFinanziamentoDaInjettare = valutaInserimento(getTipoFinanziamento(),
				classificatoreAggiornamento.getTipoFinanziamento(), isTipoFinanziamentoEditabile());
		TipoFondo tipoFondoDaInjettare = valutaInserimento(getTipoFondo(), classificatoreAggiornamento.getTipoFondo(),
				isTipoFondoEditabile());

		SiopeSpesa siopeSpesaDaInjettare = valutaInserimento(getSiopeSpesa(),
				classificatoreAggiornamento.getSiopeSpesa(), isSiopeSpesaEditabile());
		RicorrenteSpesa ricorrenteSpesaDaInjettare = valutaInserimento(getRicorrenteSpesa(),
				classificatoreAggiornamento.getRicorrenteSpesa(), isRicorrenteSpesaEditabile());
		PerimetroSanitarioSpesa perimetroSanitarioSpesadaInjettare = valutaInserimento(getPerimetroSanitarioSpesa(),
				classificatoreAggiornamento.getPerimetroSanitarioSpesa(), isPerimetroSanitarioSpesaEditabile());
		TransazioneUnioneEuropeaSpesa transazioneUnioneEuropeaSpesaDaInjettare = valutaInserimento(
				getTransazioneUnioneEuropeaSpesa(), classificatoreAggiornamento.getTransazioneUnioneEuropeaSpesa(),
				isTransazioneUnioneEuropeaSpesaEditabile());
		PoliticheRegionaliUnitarie politicheRegionaliUnitarieDaInjettare = valutaInserimento(
				getPoliticheRegionaliUnitarie(), classificatoreAggiornamento.getPoliticheRegionaliUnitarie(),
				isPoliticheRegionaliUnitarieEditabile());
		RisorsaAccantonata risorsaAccantonataDaInjettare = valutaInserimento(getRisorsaAccantonata(), classificatoreAggiornamento.getRisorsaAccantonata(), true);

		// Injezione dei classificatori nella request
		capitoloUscitaGestione.setEnte(getEnte());
		capitoloUscitaGestione.setBilancio(getBilancio());
		capitoloUscitaGestione.setMissione(missioneDaInjettare);
		capitoloUscitaGestione.setProgramma(programmaDaInjettare);
		capitoloUscitaGestione.setTitoloSpesa(titoloSpesaDaInjettare);
		capitoloUscitaGestione.setMacroaggregato(macroaggregatoDaInjettare);
		capitoloUscitaGestione.setElementoPianoDeiConti(elementoPianoDeiContiDaInjettare);
		capitoloUscitaGestione.setStrutturaAmministrativoContabile(strutturaAmministrativoContabileDaInjettare);
		capitoloUscitaGestione
				.setClassificazioneCofogProgramma(getClassificazioneCofogProgramma(classificazioneCofogDaInjettare));
		capitoloUscitaGestione.setTipoFinanziamento(tipoFinanziamentoDaInjettare);
		capitoloUscitaGestione.setTipoFondo(tipoFondoDaInjettare);

		capitoloUscitaGestione.setSiopeSpesa(siopeSpesaDaInjettare);
		capitoloUscitaGestione.setRicorrenteSpesa(ricorrenteSpesaDaInjettare);
		capitoloUscitaGestione.setPerimetroSanitarioSpesa(perimetroSanitarioSpesadaInjettare);
		capitoloUscitaGestione.setTransazioneUnioneEuropeaSpesa(transazioneUnioneEuropeaSpesaDaInjettare);
		capitoloUscitaGestione.setPoliticheRegionaliUnitarie(politicheRegionaliUnitarieDaInjettare);
		//SIAC-7192
		capitoloUscitaGestione.setRisorsaAccantonata(risorsaAccantonataDaInjettare);
		
		// Gli importi sono obbligatori
		capitoloUscitaGestione.setListaImportiCapitoloUG(getListaImportiCapitolo());

		/* Gestione della lista dei classificatori generici */
		capitoloUscitaGestione
				.setClassificatoriGenerici(getListaClassificatoriGenericiAggiornamento(classificatoreAggiornamento));

		request.setCapitoloUscitaGestione(capitoloUscitaGestione);

		return request;
	}

	/**
	 * Restituisce una Request di tipo
	 * {@link RicercaVariazioniCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaVariazioniCapitoloUscitaGestione creaRequestRicercaVariazioniCapitoloUscitaGestione() {
		RicercaVariazioniCapitoloUscitaGestione request = creaRequest(RicercaVariazioniCapitoloUscitaGestione.class);

		capitoloUscitaGestione.setBilancio(getBilancio());
		capitoloUscitaGestione.setEnte(getEnte());

		request.setCapitoloUscitaGest(capitoloUscitaGestione);

		return request;
	}

	/**
	 * Restituisce una Request di tipo
	 * {@link RicercaDettaglioCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloUscitaGestione creaRequestRicercaDettaglioCapitoloUscitaGestione() {
		RicercaDettaglioCapitoloUscitaGestione request = creaRequest(RicercaDettaglioCapitoloUscitaGestione.class);

		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloUGest(getRicercaDettaglioCapitoloUGest(capitoloUscitaGestione.getUid()));

		return request;
	}

	/**
	 * Restituisce una Request di tipo
	 * {@link ControllaClassificatoriModificabiliCapitolo} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public ControllaClassificatoriModificabiliCapitolo creaRequestControllaClassificatoriModificabiliCapitolo() {
		ControllaClassificatoriModificabiliCapitolo request = creaRequest(
				ControllaClassificatoriModificabiliCapitolo.class);

		request.setBilancio(getBilancio());

		request.setEnte(getEnte());
		request.setNumeroArticolo(capitoloUscitaGestione.getNumeroArticolo());
		request.setNumeroCapitolo(capitoloUscitaGestione.getNumeroCapitolo());
		request.setNumeroUEB(capitoloUscitaGestione.getNumeroUEB());

		request.setTipoCapitolo(TipoCapitolo.CAPITOLO_USCITA_GESTIONE);

		return request;
	}

	/* Metodi di utilita' */

	/**
	 * Popola il model a partire dalla Response del servizio di Ricerca
	 * Dettaglio del Capitolo di Uscita Gestione.
	 * 
	 * @param response
	 *            la Response del servizio
	 */
	public void impostaDatiDaResponse(RicercaDettaglioCapitoloUscitaGestioneResponse response,
			List<ImportiCapitoloPerComponente> importiCapitoli) {
		capitoloUscitaGestione = response.getCapitoloUscita();

		/* Ottenuti dalla response, injettati nel capitoloUscitaPrevisione */
		setBilancio(capitoloUscitaGestione.getBilancio());
		setTipoFinanziamento(capitoloUscitaGestione.getTipoFinanziamento());
		setTipoFondo(capitoloUscitaGestione.getTipoFondo());
		setClassificazioneCofog(capitoloUscitaGestione.getClassificazioneCofog());
		setElementoPianoDeiConti(capitoloUscitaGestione.getElementoPianoDeiConti());
		setMacroaggregato(capitoloUscitaGestione.getMacroaggregato());
		setMissione(capitoloUscitaGestione.getMissione());
		setProgramma(capitoloUscitaGestione.getProgramma());
		setStrutturaAmministrativoContabile(capitoloUscitaGestione.getStrutturaAmministrativoContabile());
		setTitoloSpesa(capitoloUscitaGestione.getTitoloSpesa());
		setImportiComponentiCapitolo(importiCapitoli);
		setSiopeSpesa(capitoloUscitaGestione.getSiopeSpesa());
		setRicorrenteSpesa(capitoloUscitaGestione.getRicorrenteSpesa());
		setPerimetroSanitarioSpesa(capitoloUscitaGestione.getPerimetroSanitarioSpesa());
		setTransazioneUnioneEuropeaSpesa(capitoloUscitaGestione.getTransazioneUnioneEuropeaSpesa());
		setPoliticheRegionaliUnitarie(capitoloUscitaGestione.getPoliticheRegionaliUnitarie());
		//SIAC-7192
		setRisorsaAccantonata(capitoloUscitaGestione.getRisorsaAccantonata());

		// Importi
		if (capitoloUscitaGestione.getListaImportiCapitoloUG() != null) {
			// Dovrebbe essere sempre non-null
			try {
				importiCapitoloUscitaGestione0 = capitoloUscitaGestione.getListaImportiCapitoloUG().get(0);
				importiCapitoloUscitaGestione1 = capitoloUscitaGestione.getListaImportiCapitoloUG().get(1);
				importiCapitoloUscitaGestione2 = capitoloUscitaGestione.getListaImportiCapitoloUG().get(2);
				importiCapitoloUscitaGestioneM1 = capitoloUscitaGestione.getListaImportiCapitoloUG().get(3);
				

			} catch (IndexOutOfBoundsException e) {
				// Ignoro l'eccezione
			}
		}
		impostaClassificatoriGenericiDaLista(capitoloUscitaGestione.getClassificatoriGenerici());
		/*
		 * Stringhe di utilita' per la visualizzazione dell'elemento del piano
		 * dei conti e della struttura amministrativo contabile
		 */
		valorizzaStringheUtilita();
	}

	/**
	 * Costruisce la lista degli Importi Capitolo Uscita Gestione a partire
	 * dagli importi del Model.
	 * 
	 * @return la lista creata
	 */
	private List<ImportiCapitoloUG> getListaImportiCapitolo() {
		List<ImportiCapitoloUG> lista = new ArrayList<ImportiCapitoloUG>();

		// Carica gli importi di ogni anno
		addImportoCapitoloALista(lista, importiCapitoloUscitaGestione0, getAnnoEsercizioInt() + 0);
		addImportoCapitoloALista(lista, importiCapitoloUscitaGestione1, getAnnoEsercizioInt() + 1);
		addImportoCapitoloALista(lista, importiCapitoloUscitaGestione2, getAnnoEsercizioInt() + 2);
		return lista;
	}

	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Uscita
	 * Previsione.
	 * 
	 * @param chiaveCapitolo
	 *            la chiave univoca del capitolo
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloUGest getRicercaDettaglioCapitoloUGest(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUGest utility = new RicercaDettaglioCapitoloUGest();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}
	
	//fix jira 7122
	public DisponibilitaCapitoloUscitaGestione getDisponibilitaCapitoloUscitaGestioneAnno0() {
		return disponibilitaCapitoloUscitaGestioneAnno0;
	}

	public void setDisponibilitaCapitoloUscitaGestioneAnno0(
			DisponibilitaCapitoloUscitaGestione disponibilitaCapitoloUscitaGestioneAnno0) {
		this.disponibilitaCapitoloUscitaGestioneAnno0 = disponibilitaCapitoloUscitaGestioneAnno0;
	}

	public DisponibilitaCapitoloUscitaGestione getDisponibilitaCapitoloUscitaGestioneAnno1() {
		return disponibilitaCapitoloUscitaGestioneAnno1;
	}

	public void setDisponibilitaCapitoloUscitaGestioneAnno1(
			DisponibilitaCapitoloUscitaGestione disponibilitaCapitoloUscitaGestioneAnno1) {
		this.disponibilitaCapitoloUscitaGestioneAnno1 = disponibilitaCapitoloUscitaGestioneAnno1;
	}

	public DisponibilitaCapitoloUscitaGestione getDisponibilitaCapitoloUscitaGestioneAnno2() {
		return disponibilitaCapitoloUscitaGestioneAnno2;
	}

	public void setDisponibilitaCapitoloUscitaGestioneAnno2(
			DisponibilitaCapitoloUscitaGestione disponibilitaCapitoloUscitaGestioneAnno2) {
		this.disponibilitaCapitoloUscitaGestioneAnno2 = disponibilitaCapitoloUscitaGestioneAnno2;
	}

	public DisponibilitaCapitoloUscitaGestione getDisponibilitaCapitoloUscitaGestioneResiduo() {
		return disponibilitaCapitoloUscitaGestioneResiduo;
	}

	public void setDisponibilitaCapitoloUscitaGestioneResiduo(
			DisponibilitaCapitoloUscitaGestione disponibilitaCapitoloUscitaGestioneResiduo) {
		this.disponibilitaCapitoloUscitaGestioneResiduo = disponibilitaCapitoloUscitaGestioneResiduo;
	}
	
	public boolean isComponentiDiversiDaFresco() {
		return componentiDiversiDaFresco;
	}

	public void setComponentiDiversiDaFresco(boolean componentiDiversiDaFresco) {
		this.componentiDiversiDaFresco = componentiDiversiDaFresco;
	}

	/**
	 * Crea la request per la Ricerca Dettaglio dei componenti del Capitolo di
	 * Uscita Previsione.
	 * 
	 * @param chiaveCapitolo
	 *            la chiave univoca del capitolo
	 * 
	 * @return la request
	 */
	public RicercaComponenteImportiCapitolo creaRequestRicercaComponenteImportiCapitolo() {
		RicercaComponenteImportiCapitolo request = creaRequest(RicercaComponenteImportiCapitolo.class);
		request.setCapitolo(new CapitoloUscitaPrevisione());
		request.getCapitolo().setUid(capitoloUscitaGestione.getUid());
		return request;
	}
	
	/**
	 * Crea una Request per il servizio di
	 * {@link RicercaDisponibilitaCapitoloUscitaGestione}.
	 * 
	 * @return la request creata
	 */
	public RicercaDisponibilitaCapitoloUscitaGestione creaRequestRicercaDisponibilitaCapitoloUscitaGestione() {
		RicercaDisponibilitaCapitoloUscitaGestione req = creaRequest(RicercaDisponibilitaCapitoloUscitaGestione.class);

		req.setAnnoBilancio(getAnnoEsercizioInt());

		CapitoloUscitaGestione capitoloUscitaGestioneReq = new CapitoloUscitaGestione();
		capitoloUscitaGestioneReq.setUid(capitoloUscitaGestione.getUid());
		req.setCapitoloUscitaGestione(capitoloUscitaGestioneReq);

		return req;
	}

	public RicercaSinteticaImpegniSubImpegni creaRequestRicercaImpegniSubImpegni() {
		RicercaSinteticaImpegniSubImpegni request1 = creaRequest(RicercaSinteticaImpegniSubImpegni.class);
		request1.setEnte(getEnte());
		ParametroRicercaImpSub parametroRicercaImpSub = new ParametroRicercaImpSub();
		parametroRicercaImpSub.setUidCapitolo(capitoloUscitaGestione.getUid());
		parametroRicercaImpSub.setAnnoEsercizio(capitoloUscitaGestione.getAnnoCapitolo());
		request1.setParametroRicercaImpSub(parametroRicercaImpSub);
		request1.setNumPagina(1);
		request1.setNumRisultatiPerPagina(1);
		//
		//nasce dallo spostare nel model questa request. Secondo me non puo' funzionare correttamente, ma la lascio qui per verifiche che lo spostamento non abbia creato problemi
//				RicercaSinteticaImpegniSubImpegni request1 = new RicercaSinteticaImpegniSubImpegni();
//				request1.setAnnoBilancio(request.getAnnoBilancio());
//				request1.setEnte(sessionHandler.getEnte());
//				request1.setRichiedente(sessionHandler.getRichiedente());		
//				ParametroRicercaImpSub parametroRicercaImpSub = new ParametroRicercaImpSub();
//				parametroRicercaImpSub.setUidCapitolo(request.getCapitoloUscitaGestione().getUid());
//				parametroRicercaImpSub.setAnnoEsercizio(request.getCapitoloUscitaGestione().getAnnoCapitolo());
//				request1.setParametroRicercaImpSub(parametroRicercaImpSub);
//				request1.setNumPagina(1);
//				request1.setNumRisultatiPerPagina(1);
		return request1;
	}

}
