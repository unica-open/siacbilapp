/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscprev;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaComponenteTabellaImportiCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaImportoTabellaImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;

/**
 * Classe di model per la consultazione del Capitolo di Uscita Previsione.
 * Contiene una mappatura dei form per il Capitolo di Uscita Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.1.2 27/06/2013
 *
 */
public class ConsultaCapitoloUscitaPrevisioneModel extends CapitoloUscitaPrevisioneModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -8593581741059902904L;
	
	
	//TODO da uniformare gli attributi da utilizzare una volta a disposizione i finali e iniziali.
	//Al momento, vengono utilizzate due gestioni differenti: quando si avrà una gestione uniforme,
	//vanno modificati anche i model eliminando attributi obsoleti o ripetuti.
	/* Maschera 1 */
	private CapitoloUscitaPrevisione capitolo;

	private ImportiCapitoloUG importiEx;
	
	private ImportiCapitoloUG importiEx1;
	private ImportiCapitoloUG importiEx2;
	private ImportiCapitoloUG importiEx3;
	
	private ImportiCapitoloUP importiCapitoloUscitaPrevisione0;
	private ImportiCapitoloUP importiCapitoloUscitaPrevisione1;
	private ImportiCapitoloUP importiCapitoloUscitaPrevisione2;

	// 6881 NUOVI DATI
	private ImportiCapitoloPerComponente competenzaStanziamento;
	private ImportiCapitoloPerComponente competenzaImpegnato;
	private ImportiCapitoloPerComponente disponibilita;
	private ImportiCapitoloPerComponente residuiEffettivi;
	private ImportiCapitoloPerComponente residuiPresunti;
	private ImportiCapitoloPerComponente cassaStanziato;
	private ImportiCapitoloPerComponente cassaPagato;

	// 6881 NUOVI DATI
	private ImportiCapitoloPerComponente competenzaStanziamentoEq;
	private ImportiCapitoloPerComponente competenzaImpegnatoEq;
	private ImportiCapitoloPerComponente disponibilitaEq;
	private ImportiCapitoloPerComponente residuiEffettiviEq;
	private ImportiCapitoloPerComponente residuiPresuntiEq;
	private ImportiCapitoloPerComponente cassaStanziatoEq;
	private ImportiCapitoloPerComponente cassaPagatoEq;

	// Modifica Tab Capitolo Gestione Equivalente
	private boolean capGestioneEquivalentePresente;

	// GESC0014
	private List<ImportiCapitoloPerComponente> importiComponentiCapitolo = new ArrayList<ImportiCapitoloPerComponente>();

	private List<ImportiCapitoloPerComponente> importiComponentiCapitoloEquiv = new ArrayList<ImportiCapitoloPerComponente>();

	private ImportiCapitolo importiAnniSuccessivi;
	private ImportiCapitolo importiResidui;
	private ImportiCapitolo importiAnniSuccessiviEquiv;
	private ImportiCapitolo importiResiduiEquiv;

	// Per il dettaglio
	private Boolean variazioneInAumento;
	// SIAC-6305
	private String openTab;
	

	/** Costruttore vuoto di default */
	public ConsultaCapitoloUscitaPrevisioneModel() {
		super();
		setTitolo("Consulta Capitolo Spesa Previsione");
	}

	/* Getter e setter */

	/**
	 * @return the capitoloUscitaPrevisione
	 */
	public CapitoloUscitaPrevisione getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo
	 *            the capitoloUscitaPrevisione to set
	 */
	public void setCapitolo(CapitoloUscitaPrevisione capitolo) {
		this.capitolo = capitolo;
	}

	/**
	 * @return the importiEx
	 */
	public ImportiCapitoloUG getImportiEx() {
		return importiEx;
	}

	/**
	 * @param importiEx
	 *            the importiEx to set
	 */
	public void setImportiEx(ImportiCapitoloUG importiEx) {
		this.importiEx = importiEx;
	}

	/*** GETTER SETTER CAPITOLO EQUIVALENTE ****/
	public ImportiCapitoloUG getImportiEx1() {
		return importiEx1;
	}

	public void setImportiEx1(ImportiCapitoloUG importiEx1) {
		this.importiEx1 = importiEx1;
	}

	public ImportiCapitoloUG getImportiEx2() {
		return importiEx2;
	}

	public void setImportiEx2(ImportiCapitoloUG importiEx2) {
		this.importiEx2 = importiEx2;
	}

	public ImportiCapitoloUG getImportiEx3() {
		return importiEx3;
	}

	public void setImportiEx3(ImportiCapitoloUG importiEx3) {
		this.importiEx3 = importiEx3;
	}

	/* FINE GETTER SETTER CAPITOLO EQUIVALENTE */

	public boolean isCapGestioneEquivalentePresente() {
		return capGestioneEquivalentePresente;
	}

	public void setCapGestioneEquivalentePresente(boolean capGestioneEquivalentePresente) {
		this.capGestioneEquivalentePresente = capGestioneEquivalentePresente;
	}

	public ImportiCapitolo getImportiAnniSuccessivi() {
		return importiAnniSuccessivi;
	}

	public void setImportiAnniSuccessivi(ImportiCapitolo importiAnniSuccessivi) {
		this.importiAnniSuccessivi = importiAnniSuccessivi;
	}

	public ImportiCapitolo getImportiResidui() {
		return importiResidui;
	}

	public void setImportiResidui(ImportiCapitolo importiCapitolo) {
		this.importiResidui = importiCapitolo;
	}

	public ImportiCapitolo getImportiAnniSuccessiviEquiv() {
		return importiAnniSuccessiviEquiv;
	}

	public void setImportiAnniSuccessiviEquiv(ImportiCapitolo importiAnniSuccessiviEquiv) {
		this.importiAnniSuccessiviEquiv = importiAnniSuccessiviEquiv;
	}

	public ImportiCapitolo getImportiResiduiEquiv() {
		return importiResiduiEquiv;
	}

	public void setImportiResiduiEquiv(ImportiCapitolo importiResiduiEquiv) {
		this.importiResiduiEquiv = importiResiduiEquiv;
	}

	/**
	 * @return the importiCapitoloUscitaPrevisione0
	 */
	public ImportiCapitoloUP getImportiCapitoloUscitaPrevisione0() {
		return importiCapitoloUscitaPrevisione0;
	}

	/**
	 * @param importiCapitoloUscitaPrevisione0
	 *            the importiCapitoloUscitaPrevisione0 to set
	 */
	public void setImportiCapitoloUscitaPrevisione0(ImportiCapitoloUP importiCapitoloUscitaPrevisione0) {
		this.importiCapitoloUscitaPrevisione0 = importiCapitoloUscitaPrevisione0;
	}

	/**
	 * @return the importiCapitoloUscitaPrevisione1
	 */
	public ImportiCapitoloUP getImportiCapitoloUscitaPrevisione1() {
		return importiCapitoloUscitaPrevisione1;
	}

	/**
	 * @param importiCapitoloUscitaPrevisione1
	 *            the importiCapitoloUscitaPrevisione1 to set
	 */
	public void setImportiCapitoloUscitaPrevisione1(ImportiCapitoloUP importiCapitoloUscitaPrevisione1) {
		this.importiCapitoloUscitaPrevisione1 = importiCapitoloUscitaPrevisione1;
	}

	/**
	 * @return the importiCapitoloUscitaPrevisione2
	 */
	public ImportiCapitoloUP getImportiCapitoloUscitaPrevisione2() {
		return importiCapitoloUscitaPrevisione2;
	}

	/**
	 * @param importiCapitoloUscitaPrevisione2
	 *            the importiCapitoloUscitaPrevisione2 to set
	 */
	public void setImportiCapitoloUscitaPrevisione2(ImportiCapitoloUP importiCapitoloUscitaPrevisione2) {
		this.importiCapitoloUscitaPrevisione2 = importiCapitoloUscitaPrevisione2;
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

	public ImportiCapitoloPerComponente getCompetenzaStanziamentoEq() {
		return competenzaStanziamentoEq;
	}

	public void setCompetenzaStanziamentoEq(ImportiCapitoloPerComponente competenzaStanziamentoEq) {
		this.competenzaStanziamentoEq = competenzaStanziamentoEq;
	}

	public ImportiCapitoloPerComponente getCompetenzaImpegnatoEq() {
		return competenzaImpegnatoEq;
	}

	public void setCompetenzaImpegnatoEq(ImportiCapitoloPerComponente competenzaImpegnatoEq) {
		this.competenzaImpegnatoEq = competenzaImpegnatoEq;
	}

	public ImportiCapitoloPerComponente getDisponibilitaEq() {
		return disponibilitaEq;
	}

	public void setDisponibilitaEq(ImportiCapitoloPerComponente disponibilitaEq) {
		this.disponibilitaEq = disponibilitaEq;
	}

	public ImportiCapitoloPerComponente getResiduiEffettiviEq() {
		return residuiEffettiviEq;
	}

	public void setResiduiEffettiviEq(ImportiCapitoloPerComponente residuiEffettiviEq) {
		this.residuiEffettiviEq = residuiEffettiviEq;
	}

	public ImportiCapitoloPerComponente getResiduiPresuntiEq() {
		return residuiPresuntiEq;
	}

	public void setResiduiPresuntiEq(ImportiCapitoloPerComponente residuiPresuntiEq) {
		this.residuiPresuntiEq = residuiPresuntiEq;
	}

	public ImportiCapitoloPerComponente getCassaStanziatoEq() {
		return cassaStanziatoEq;
	}

	public void setCassaStanziatoEq(ImportiCapitoloPerComponente cassaStanziatoEq) {
		this.cassaStanziatoEq = cassaStanziatoEq;
	}

	public ImportiCapitoloPerComponente getCassaPagatoEq() {
		return cassaPagatoEq;
	}

	public void setCassaPagatoEq(ImportiCapitoloPerComponente cassaPagatoEq) {
		this.cassaPagatoEq = cassaPagatoEq;
	}

	/**
	 * @return the variazioneInAumento
	 */
	public Boolean getVariazioneInAumento() {
		return variazioneInAumento;
	}

	/**
	 * @param variazioneInAumento
	 *            the variazioneInAumento to set
	 */
	public void setVariazioneInAumento(Boolean variazioneInAumento) {
		this.variazioneInAumento = variazioneInAumento;
	}

	/**
	 * @return the openTab
	 */
	public String getOpenTab() {
		return this.openTab;
	}

	/**
	 * @param openTab
	 *            the openTab to set
	 */
	public void setOpenTab(String openTab) {
		this.openTab = openTab;
	}

	/**
	 * Ottiene l'uid del capitolo da consultare.
	 * 
	 * @return l'uid del Capitolo di Uscita Previsione da consultare
	 */
	public int getUidDaConsultare() {
		return capitolo == null ? 0 : capitolo.getUid();
	}

	/**
	 * Imposta l'uid nel Capitolo di Uscita Previsione da consultare.
	 * 
	 * @param uidDaConsultare
	 *            l'uid da impostare
	 */
	public void setUidDaConsultare(int uidDaConsultare) {
		if (capitolo == null) {
			capitolo = new CapitoloUscitaPrevisione();
		}
		capitolo.setUid(uidDaConsultare);
	}

	/* COmponenti capitolo */
	public List<ImportiCapitoloPerComponente> getImportiComponentiCapitolo() {
		return importiComponentiCapitolo;
	}

	public List<ImportiCapitoloPerComponente> getImportiComponentiCapitoloEquiv() {
		return importiComponentiCapitoloEquiv;
	}

	public void setImportiComponentiCapitolo(List<ImportiCapitoloPerComponente> importiComponentiCapitolo) {
		for (ImportiCapitoloPerComponente icc : importiComponentiCapitolo) {
			this.importiComponentiCapitolo.add(icc);
		}
	}

	public void setImportiComponentiCapitoloEquiv(List<ImportiCapitoloPerComponente> importiComponentiCapitoloEquiv) {
		this.importiComponentiCapitoloEquiv = importiComponentiCapitoloEquiv;
	}

	/* Request */

	/**
	 * Crea una Request per il servizio di
	 * {@link RicercaDettaglioCapitoloUscitaPrevisione}.
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloUscitaPrevisione creaRequestRicercaDettaglioCapitoloUscitaPrevisione() {
		RicercaDettaglioCapitoloUscitaPrevisione request = creaRequest(RicercaDettaglioCapitoloUscitaPrevisione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloUPrev(
				creaUtilitaRicercadettaglioCapitoloUscitaPrevisione(capitolo.getUid()));
		request.setImportiDerivatiRichiesti(EnumSet.allOf(ImportiCapitoloEnum.class));

		return request;
	}

	/* Metodi di utilita' */

	/**
	 * Metodo di utilit&agrave; per la Ricerca Dettaglio del Capitolo di Uscita
	 * Previsione.
	 * 
	 * @param chiaveCapitolo
	 *            la chiave univoca del capitolo
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloUPrev creaUtilitaRicercadettaglioCapitoloUscitaPrevisione(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUPrev utility = new RicercaDettaglioCapitoloUPrev();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}

	/**
	 * Imposta i dati nel model a partire dalla Response del servizio di
	 * {@link RicercaDettaglioCapitoloUscitaPrevisione} e dalla sessione.
	 * 
	 * @param response
	 *            la Response del servizio di ricerca dettaglio
	 */
	public void impostaDatiDaResponse(RicercaDettaglioCapitoloUscitaPrevisioneResponse response) {

		/* Ottenuti dalla response */
		setBilancio(response.getBilancio());
		capitolo = response.getCapitoloUscitaPrevisione();
		setTipoFinanziamento(response.getTipoFinanziamento());
		setTipoFondo(response.getTipoFondo());

		/* Ottenuti dalla sessione */
		impostaClassificatoriGenericiDaLista(response.getCapitoloUscitaPrevisione().getClassificatoriGenerici());

		/* Ottenuti indirettamente dalla response */
		impostaImportiCapitoloUscitaPrevisione(response.getListaImportiCapitoloUP());
		impostaImportoEx(capitolo);

		/* Ottenuti dalla response, injettati nel capitoloUscitaPrevisione */
		setClassificazioneCofog(capitolo.getClassificazioneCofog());
		setElementoPianoDeiConti(capitolo.getElementoPianoDeiConti());
		setMacroaggregato(capitolo.getMacroaggregato());
		setMissione(capitolo.getMissione());
		setProgramma(capitolo.getProgramma());
		setStrutturaAmministrativoContabile(capitolo.getStrutturaAmministrativoContabile());
		setTitoloSpesa(capitolo.getTitoloSpesa());

		setSiopeSpesa(capitolo.getSiopeSpesa());
		setRicorrenteSpesa(capitolo.getRicorrenteSpesa());
		setPerimetroSanitarioSpesa(capitolo.getPerimetroSanitarioSpesa());
		setTransazioneUnioneEuropeaSpesa(capitolo.getTransazioneUnioneEuropeaSpesa());
		setPoliticheRegionaliUnitarie(capitolo.getPoliticheRegionaliUnitarie());

		controllaImportiCapitoloEquivalente(capitolo, ImportiCapitoloUG.class);
	}

	/**
	 * Imposta l'importo per i campi relativi all'ex.
	 * 
	 * @param capitolo
	 *            il capitolo da cui ottenere il dato
	 */
	private void impostaImportoEx(CapitoloUscitaPrevisione capitolo) {
		importiEx = capitolo.getImportiCapitoloUG();
	}

	/**
	 * Imposta gli importi del Capitolo di Uscita Previsione a partire dalla
	 * Response del servizio di
	 * {@link RicercaDettaglioCapitoloUscitaPrevisione}.
	 * 
	 * @param listaImporti
	 *            la lista degli importi
	 */
	private void impostaImportiCapitoloUscitaPrevisione(List<ImportiCapitoloUP> listaImporti) {
		Integer anno = getAnnoEsercizioInt();
		try {
			int annoInt = anno.intValue();
			importiCapitoloUscitaPrevisione0 = ComparatorUtils.searchByAnno(listaImporti, anno);
			importiCapitoloUscitaPrevisione1 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt + 1));
			importiCapitoloUscitaPrevisione2 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt + 2));
		} catch (Exception e) {
			// Ignoro l'errore: ipmorti non impostabili
		}
	}

	// GESC014
	public RicercaComponenteImportiCapitolo creaRequestRicercaComponenteImportiCapitolo() {
		RicercaComponenteImportiCapitolo request = creaRequest(RicercaComponenteImportiCapitolo.class);
		request.setCapitolo(new CapitoloUscitaPrevisione());
		request.getCapitolo().setUid(capitolo.getUid());
		return request;
	}

	// CAPITOLO EQUIVALENTE
	public RicercaDettaglioCapitoloUscitaGestione creaRequestRicercaDettaglioCapitoloUscitaGestioneEquivalente(
			int uidCapEq) {
		RicercaDettaglioCapitoloUscitaGestione request = creaRequest(RicercaDettaglioCapitoloUscitaGestione.class);
		request.setEnte(getEnte());
		RicercaDettaglioCapitoloUGest dettaglio = new RicercaDettaglioCapitoloUGest();
		dettaglio.setChiaveCapitolo(uidCapEq);
		request.setRicercaDettaglioCapitoloUGest(dettaglio);
		request.setImportiDerivatiRichiesti(EnumSet.allOf(ImportiCapitoloEnum.class));
		return request;
	}

	// CAPITOLO EQUIVALENTE
	public void impostaImportiCapitoloEquivalente(List<ImportiCapitoloUG> listaImporti) {
		Integer anno = getAnnoEsercizioInt();
		try {
			int annoInt = anno.intValue();
			importiEx1 = ComparatorUtils.searchByAnno(listaImporti, anno);
			importiEx2 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt + 1));
			importiEx3 = ComparatorUtils.searchByAnno(listaImporti, Integer.valueOf(annoInt + 2));
		} catch (Exception e) {
			// Ignoro l'errore: ipmorti non impostabili
		}
	}

	// COMPONENTI EQUIVALENTI
	public RicercaComponenteImportiCapitolo creaRequestRicercaComponenteImportiCapitoloEquivalente(int uidEq) {
		RicercaComponenteImportiCapitolo request = creaRequest(RicercaComponenteImportiCapitolo.class);
		request.setCapitolo(new CapitoloUscitaGestione());
		request.getCapitolo().setUid(uidEq);
		return request;
	}

}
