/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoScritturaPrimaNotaLibera;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.CausaleEPModelDetail;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;
import it.csi.siac.siacgenser.model.TipoRelazionePrimaNota;
/**
 * Classe base di model per l'inserimento e l'aggiornamento della PRIMA NOTA LIBERA EP.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 14/04/2015
 *
 */
public abstract class BaseInserisciAggiornaPrimaNotaLiberaBaseModel extends BasePrimaNotaLiberaModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5650709800328793574L;
	
	private Integer uidPrimaNota;

	private PrimaNota primaNotaLibera;
	private List<MovimentoEP> listaMovimentoEP = new ArrayList<MovimentoEP>();
	private BigDecimal totaleDare  = BigDecimal.ZERO;
	private BigDecimal totaleAvere = BigDecimal.ZERO;
	private BigDecimal importoDaRegistrare = BigDecimal.ZERO;
	//diff totali dare avere
	private BigDecimal daRegistrare = BigDecimal.ZERO;
	

	private List<Evento> listaEvento = new ArrayList<Evento>();

	private List<CausaleEP> listaCausaleEP = new ArrayList<CausaleEP>();
	
	private Evento evento;
	private TipoEvento tipoEvento;
	private CausaleEP causaleEP;
	
	/*
	 * STEP 2
	 * */
	private List<Conto> listaConto = new ArrayList<Conto>();
	private List<MovimentoDettaglio> listaMovimentoDettaglio = new ArrayList<MovimentoDettaglio>();
	private List<ElementoScritturaPrimaNotaLibera> listaElementoScrittura = new ArrayList<ElementoScritturaPrimaNotaLibera>();
	private List<ElementoScritturaPrimaNotaLibera> listaElementoScritturaPerElaborazione = new ArrayList<ElementoScritturaPrimaNotaLibera>();
	private List<ElementoScritturaPrimaNotaLibera> listaElementoScritturaDaCausale = new ArrayList<ElementoScritturaPrimaNotaLibera>();
	
	
	private boolean isContiCausale = false;
	private boolean singoloContoCausale = false;
	private boolean isImportoFromProposto = false;
	/*da modale*/
	private Integer indiceConto;
	private BigDecimal importo;
	private OperazioneSegnoConto operazioneSegnoConto;
	/*da collapse*/
	private Conto conto;
	private BigDecimal importoCollapse;
	private OperazioneSegnoConto operazioneSegnoContoCollapse;
	
	//collegamento prime note
	private Integer indicePrimaNota;
	private List<TipoRelazionePrimaNota> listaMotivazioni = new ArrayList<TipoRelazionePrimaNota>();
	private List <TipoCausale> listaTipoPrimaNota = Arrays.asList(TipoCausale.values());
	private TipoRelazionePrimaNota motivazione;
	private String noteCollegamento;
	private List<PrimaNota> listaPrimeNoteDaCollegare = new ArrayList<PrimaNota>();
	private PrimaNota primaNotaDaCollegare;
	private Integer annoPrimaNota;
	
	/* modale compilazione guidata prima nota da collegare*/
	private List<TipoEvento> listaTipiEvento = new ArrayList<TipoEvento>();
	
	/* modale compilazione guidata conto*/
	private List<ClassePiano> listaClassi = new ArrayList<ClassePiano>();
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	
	// SIAC-5281
	private Missione missione;
	private Programma programma;
	private List<Missione> listaMissione = new ArrayList<Missione>();
	
	// SIAC-5336
	private ClassificatoreGSA classificatoreGSA;
	
	/** Costruttore vuoto di default */ 
	public BaseInserisciAggiornaPrimaNotaLiberaBaseModel(){
		setTitolo("Inserisci Prima Nota Libera");
	}
	
	/**
	 * @return l'ambito (pu&oacute; essere AMBITO_FIN o AMBITO_GSA)
	 */
	
	public abstract Ambito getAmbito();
	
	/**
	 * @return la stringa del codice dell'ambito, pu&oacute; essere "FIN" o "GSA"
	 */
	public String getAmbitoSuffix() {
		return getAmbito().getSuffix();
	}
	
	/**
	 * 
	 * @return la stringa del codice dell'ambito, pu&oacute; essere "FIN" o "GSA"
	 */
	public Ambito getAmbitoFIN(){
		return Ambito.AMBITO_FIN;
	}
	
	/**
	 * @return the denominazioneWizard
	 */
	public abstract String getDenominazioneWizard();
	
	/**
	 * @return the aggiornamento
	 */
	public abstract boolean isAggiornamento();
	
	/**
	 * @return the uidPrimaNota
	 */
	public Integer getUidPrimaNota() {
		return uidPrimaNota;
	}


	/**
	 * @param uidPrimaNota the uidPrimaNota to set
	 */
	public void setUidPrimaNota(Integer uidPrimaNota) {
		this.uidPrimaNota = uidPrimaNota;
	}


	/**
	 * @return the primaNotaLibera
	 */
	public PrimaNota getPrimaNotaLibera() {
		return primaNotaLibera;
	}


	/**
	 * @param primaNotaLibera the primaNotaLibera to set
	 */
	public void setPrimaNotaLibera(PrimaNota primaNotaLibera) {
		this.primaNotaLibera = primaNotaLibera;
	}



	/**
	 * @return the listaMovimentoEP
	 */
	public List<MovimentoEP> getListaMovimentoEP() {
		return listaMovimentoEP;
	}

	/**
	 * @param listaMovimentoEP the listaMovimentoEP to set
	 */
	public void setListaMovimentoEP(List<MovimentoEP> listaMovimentoEP) {
		this.listaMovimentoEP = listaMovimentoEP;
	}

	/**
	 * @return the totaleDare
	 */
	public BigDecimal getTotaleDare() {
		return totaleDare;
	}

	/**
	 * @param totaleDare the totaleDare to set
	 */
	public void setTotaleDare(BigDecimal totaleDare) {
		this.totaleDare = totaleDare;
	}

	/**
	 * @return the totaleAvere
	 */
	public BigDecimal getTotaleAvere() {
		return totaleAvere;
	}

	/**
	 * @param totaleAvere the totaleAvere to set
	 */
	public void setTotaleAvere(BigDecimal totaleAvere) {
		this.totaleAvere = totaleAvere;
	}

	

	/**
	 * @return the importoDaRegistrare
	 */
	public BigDecimal getImportoDaRegistrare() {
		return importoDaRegistrare;
	}

	/**
	 * @param importoDaRegistrare the importoDaRegistrare to set
	 */
	public void setImportoDaRegistrare(BigDecimal importoDaRegistrare) {
		this.importoDaRegistrare = importoDaRegistrare;
	}

	/**
	 * @return the daRegistrare
	 */
	public BigDecimal getDaRegistrare() {
		return daRegistrare;
	}

	/**
	 * @param daRegistrare the daRegistrare to set
	 */
	public void setDaRegistrare(BigDecimal daRegistrare) {
		this.daRegistrare = daRegistrare;
	}

	/**
	 * @return the listaEvento
	 */
	public List<Evento> getListaEvento() {
		return listaEvento;
	}


	/**
	 * @param listaEvento the listaEvento to set
	 */
	public void setListaEvento(List<Evento> listaEvento) {
		this.listaEvento = (listaEvento==null) ? new ArrayList<Evento>() : listaEvento;
	}




	/**
	 * @return the evento
	 */
	public Evento getEvento() {
		return evento;
	}


	/**
	 * @param evento the evento to set
	 */
	public void setEvento(Evento evento) {
		this.evento = evento;
	}


	/**
	 * @return the tipoEvento
	 */
	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}


	/**
	 * @param tipoEvento the tipoEvento to set
	 */
	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}


	/**
	 * @return the listaCausaleEP
	 */
	public List<CausaleEP> getListaCausaleEP() {
		return listaCausaleEP;
	}


	/**
	 * @param listaCausaleEP the listaCausaleEP to set
	 */
	public void setListaCausaleEP(List<CausaleEP> listaCausaleEP) {
		this.listaCausaleEP = listaCausaleEP;
	}


	/**
	 * @return the causaleEP
	 */
	public CausaleEP getCausaleEP() {
		return causaleEP;
	}


	/**
	 * @param causaleEP the causaleEp to set
	 */
	public void setCausaleEP(CausaleEP causaleEP) {
		this.causaleEP = causaleEP;
	}

	/**
	 * @return the indiceConto
	 */
	public Integer getIndiceConto() {
		return indiceConto;
	}

	/**
	 * @param indiceConto the indiceConto to set
	 */
	public void setIndiceConto(Integer indiceConto) {
		this.indiceConto = indiceConto;
	}
	/**
	 * @return the stringaRiepiloCausaleEPStep1
	 */
	public String getStringaRiepiloCausaleEPStep1() {
		return computaStringaCausaleEPPrimaNota();
	}

	/**
	 * @return the stringaRiepiloDescrizioneStep1
	 */
	public String getStringaRiepiloDescrizioneStep1() {
		return computaStringaDescrizionePrimaNota();
	}

	
	/**
	 * @return the TitoloRiepilogoPrimaNotaStep3
	 */
	public String getTitoloRiepilogoPrimaNotaStep3() {
		return computaTitoloStep3PrimaNota();
	}
	 
	/**
	 * @return the listaConto
	 */
	public List<Conto> getListaConto() {
		return listaConto;
	}

	/**
	 * @param listaConto the listaConto to set
	 */
	public void setListaConto(List<Conto> listaConto) {
		this.listaConto = listaConto;
	}

	/**
	 * @return the conto
	 */
	public Conto getConto() {
		return conto;
	}

	/**
	 * @param conto the conto to set
	 */
	public void setConto(Conto conto) {
		this.conto = conto;
	}

	/**
	 * @return the listaMovimentoDettaglio
	 */
	public List<MovimentoDettaglio> getListaMovimentoDettaglio() {
		return listaMovimentoDettaglio;
	}

	/**
	 * @param listaMovimentoDettaglio the listaMovimentoDettaglio to set
	 */
	public void setListaMovimentoDettaglio(List<MovimentoDettaglio> listaMovimentoDettaglio) {
		this.listaMovimentoDettaglio = listaMovimentoDettaglio;
	}

	/**
	 * @return the listaElementoScrittura
	 */
	public List<ElementoScritturaPrimaNotaLibera> getListaElementoScrittura() {
		return listaElementoScrittura;
	}

	/**
	 * @param listaElementoScrittura the listaElementoScrittura to set
	 */
	public void setListaElementoScrittura(List<ElementoScritturaPrimaNotaLibera> listaElementoScrittura) {
		this.listaElementoScrittura = listaElementoScrittura;
	}
	
	/**
	 * @return the listaElementoScritturaPerElaborazione
	 */
	public List<ElementoScritturaPrimaNotaLibera> getListaElementoScritturaPerElaborazione() {
		return listaElementoScritturaPerElaborazione;
	}

	/**
	 * @param listaElementoScritturaPerElaborazione the listaElementoScritturaPerElaborazione to set
	 */
	public void setListaElementoScritturaPerElaborazione(List<ElementoScritturaPrimaNotaLibera> listaElementoScritturaPerElaborazione) {
		this.listaElementoScritturaPerElaborazione = listaElementoScritturaPerElaborazione;
	}
	/**
	 * @return the listaElementoScritturaDaCausale
	 */
	public List<ElementoScritturaPrimaNotaLibera> getListaElementoScritturaDaCausale() {
		return listaElementoScritturaDaCausale;
	}

	/**
	 * @param listaElementoScritturaDaCausale the listaElementoScritturaDaCausale to set
	 */
	public void setListaElementoScritturaDaCausale(
			List<ElementoScritturaPrimaNotaLibera> listaElementoScritturaDaCausale) {
		this.listaElementoScritturaDaCausale = listaElementoScritturaDaCausale;
	}

	/**
	 * @return the isContiCausale
	 */
	public boolean isContiCausale() {
		return isContiCausale;
	}

	/**
	 * @param isContiCausale the isContiCausale to set
	 */
	public void setContiCausale(boolean isContiCausale) {
		this.isContiCausale = isContiCausale;
	}

	/**
	 * @return the singoloContoCausale
	 */
	public boolean isSingoloContoCausale() {
		return singoloContoCausale;
	}

	/**
	 * @param singoloContoCausale the singoloContoCausale to set
	 */
	public void setSingoloContoCausale(boolean singoloContoCausale) {
		this.singoloContoCausale = singoloContoCausale;
	}

	/**
	 * @return the isImportoFromProposto
	 */
	public boolean isImportoFromProposto() {
		return isImportoFromProposto;
	}

	/**
	 * @param isImportoFromProposto the isImportoFromProposto to set
	 */
	public void setImportoFromProposto(boolean isImportoFromProposto) {
		this.isImportoFromProposto = isImportoFromProposto;
	}

	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return importo;
	}

	/**
	 * @param importo the importo to set
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	/**
	 * @return the operazioneSegnoConto
	 */
	public OperazioneSegnoConto getOperazioneSegnoConto() {
		return operazioneSegnoConto;
	}

	/**
	 * @param operazioneSegnoConto the operazioneSegnoConto to set
	 */
	public void setOperazioneSegnoConto(OperazioneSegnoConto operazioneSegnoConto) {
		this.operazioneSegnoConto = operazioneSegnoConto;
	}

	/**
	 * @return the importoCollapse
	 */
	public BigDecimal getImportoCollapse() {
		return importoCollapse;
	}

	/**
	 * @param importoCollapse the importoCollapse to set
	 */
	public void setImportoCollapse(BigDecimal importoCollapse) {
		this.importoCollapse = importoCollapse;
	}

	/**
	 * @return the operazioneSegnoContoCollapse
	 */
	public OperazioneSegnoConto getOperazioneSegnoContoCollapse() {
		return operazioneSegnoContoCollapse;
	}

	/**
	 * @param operazioneSegnoContoCollapse the operazioneSegnoContoCollapse to set
	 */
	public void setOperazioneSegnoContoCollapse(OperazioneSegnoConto operazioneSegnoContoCollapse) {
		this.operazioneSegnoContoCollapse = operazioneSegnoContoCollapse;
	}
	
	/**
	 * @return the indicePrimaNota
	 */
	public Integer getIndicePrimaNota() {
		return indicePrimaNota;
	}

	/**
	 * @param indicePrimaNota the indicePrimaNota to set
	 */
	public void setIndicePrimaNota(Integer indicePrimaNota) {
		this.indicePrimaNota = indicePrimaNota;
	}

	/**
	 * @return the listaMotivazioni
	 */
	public List<TipoRelazionePrimaNota> getListaMotivazioni() {
		return listaMotivazioni;
	}

	/**
	 * @param listaMotivazioni the listaMotivazioni to set
	 */
	public void setListaMotivazioni(List<TipoRelazionePrimaNota> listaMotivazioni) {
		this.listaMotivazioni = listaMotivazioni;
	}

	/**
	 * @return the listaTipoPrimaNota
	 */
	public List<TipoCausale> getListaTipoPrimaNota() {
		return listaTipoPrimaNota;
	}

	/**
	 * @param listaTipoPrimaNota the listaTipoPrimaNota to set
	 */
	public void setListaTipoPrimaNota(List<TipoCausale> listaTipoPrimaNota) {
		this.listaTipoPrimaNota = listaTipoPrimaNota;
	}

	/**
	 * @return the motivazione
	 */
	public TipoRelazionePrimaNota getMotivazione() {
		return motivazione;
	}

	/**
	 * @param motivazione the motivazione to set
	 */
	public void setMotivazione(TipoRelazionePrimaNota motivazione) {
		this.motivazione = motivazione;
	}
	
	/**
	 * @return the noteCollegamento
	 */
	public String getNoteCollegamento() {
		return noteCollegamento;
	}

	/**
	 * @param noteCollegamento the noteCollegamento to set
	 */
	public void setNoteCollegamento(String noteCollegamento) {
		this.noteCollegamento = noteCollegamento;
	}

	/**
	 * @return the listaTipiEvento
	 */
	public List<TipoEvento> getListaTipiEvento() {
		return listaTipiEvento;
	}

	/**
	 * @param listaTipiEvento the listaTipiEvento to set
	 */
	public void setListaTipiEvento(List<TipoEvento> listaTipiEvento) {
		this.listaTipiEvento = listaTipiEvento;
	}
	
	/**
	 * @return the listaPrimeNoteDaCollegare
	 */
	public List<PrimaNota> getListaPrimeNoteDaCollegare() {
		return listaPrimeNoteDaCollegare;
	}

	/**
	 * @param listaPrimeNoteDaCollegare the listaPrimeNoteDaCollegare to set
	 */
	public void setListaPrimeNoteDaCollegare(
			List<PrimaNota> listaPrimeNoteDaCollegare) {
		this.listaPrimeNoteDaCollegare = listaPrimeNoteDaCollegare;
	}
	
	/**
	 * @return the primaNotaDaCollegare
	 */
	public PrimaNota getPrimaNotaDaCollegare() {
		return primaNotaDaCollegare;
	}

	/**
	 * @param primaNotaDaCollegare the primaNotaDaCollegare to set
	 */
	public void setPrimaNotaDaCollegare(PrimaNota primaNotaDaCollegare) {
		this.primaNotaDaCollegare = primaNotaDaCollegare;
	}
	
	/**
	 * @return the annoPrimaNota
	 */
	public Integer getAnnoPrimaNota() {
		return annoPrimaNota;
	}

	/**
	 * @param annoPrimaNota the annoPrimaNota to set
	 */
	public void setAnnoPrimaNota(Integer annoPrimaNota) {
		this.annoPrimaNota = annoPrimaNota;
	}

	/**
	 * @return the listaClassi
	 */
	public final List<ClassePiano> getListaClassi() {
		return listaClassi;
	}

	/**
	 * @param listaClassi the listaClassi to set
	 */
	public final void setListaClassi(List<ClassePiano> listaClassi) {
		this.listaClassi = listaClassi;
	}

	/**
	 * @return the listaTitoloEntrata
	 */
	public final List<TitoloEntrata> getListaTitoloEntrata() {
		return listaTitoloEntrata;
	}

	/**
	 * @param listaTitoloEntrata the listaTitoloEntrata to set
	 */
	public final void setListaTitoloEntrata(List<TitoloEntrata> listaTitoloEntrata) {
		this.listaTitoloEntrata = listaTitoloEntrata != null ? listaTitoloEntrata : new ArrayList<TitoloEntrata>();
	}

	/**
	 * @return the listaTitoloSpesa
	 */
	public final List<TitoloSpesa> getListaTitoloSpesa() {
		return listaTitoloSpesa;
	}

	/**
	 * @param listaTitoloSpesa the listaTitoloSpesa to set
	 */
	public final void setListaTitoloSpesa(List<TitoloSpesa> listaTitoloSpesa) {
		this.listaTitoloSpesa = listaTitoloSpesa != null ? listaTitoloSpesa : new ArrayList<TitoloSpesa>();
	}

	/**
	 * @return the missione
	 */
	public Missione getMissione() {
		return missione;
	}

	/**
	 * @param missione the missione to set
	 */
	public void setMissione(Missione missione) {
		this.missione = missione;
	}

	/**
	 * @return the programma
	 */
	public Programma getProgramma() {
		return programma;
	}

	/**
	 * @param programma the programma to set
	 */
	public void setProgramma(Programma programma) {
		this.programma = programma;
	}

	/**
	 * @return the listaMissione
	 */
	public List<Missione> getListaMissione() {
		return listaMissione;
	}

	/**
	 * @param listaMissione the listaMissione to set
	 */
	public void setListaMissione(List<Missione> listaMissione) {
		this.listaMissione = listaMissione != null ? listaMissione : new ArrayList<Missione>();
	}
	
	/**
	 * @return the classificatoreGSA
	 */
	public ClassificatoreGSA getClassificatoreGSA() {
		return classificatoreGSA;
	}

	/**
	 * Sets the classificatore GSA.
	 *
	 * @param classificatoreGSA the new classificatore GSA
	 */
	public void setClassificatoreGSA(ClassificatoreGSA classificatoreGSA) {
		this.classificatoreGSA = classificatoreGSA;
	}

	/**
	 * @return the baseUrl
	 */
	public abstract String getBaseUrl();
	
	
	/**
	 * @return the urlAnnullaStep1
	 */
	public String getUrlAnnullaStep1(){
		return getBaseUrl() + "_annullaStep1.do";
	}
	
	/**
	 * @return the urlAnnullaStep2
	 */
	public  String getUrlAnnullaStep2(){
		return getBaseUrl() + "_annullaStep2.do";
	}
	
	/**
	 * @return the urlStep1
	 */
	public String getUrlStep1() {
		return getBaseUrl() + "_completeStep1";
	}
	
	/**
	 * @return the urlBackToStep1
	 */
	public String getUrlBackToStep1() {
		return getBaseUrl() + "_backToStep1.do";
	}
	
	/**
	 * @return the urlStep2
	 */
	public String getUrlStep2() {
		return getBaseUrl() + "_completeStep2";
	}
	
	/**
	 * @return the urlStep3
	 */
	public String getUrlStep3() {
		return getBaseUrl() + "_completeStep3";
	}
	
	/**
	 * @return the tipoCausale
	 */
	public TipoCausale getTipoCausale() {
		return TipoCausale.Libera;
	}
	
	/**
	 * Checks if is validazione.
	 *
	 * @return true, if is validazione
	 */
	public boolean isValidazione() {
		return false;
	}
	
	/**
	 * @return the inserisciNuoviContiAbilitato
	 */
	public abstract boolean isInserisciNuoviContiAbilitato();
	
	
	
	/**
	 * Calcolo della stringa del riepilogo causale
	 * 
	 * @return la stringa del numero richiesta
	 */
	protected String computaStringaCausaleEPPrimaNota() {

		StringBuilder sb = new StringBuilder();
		if((causaleEP != null) && (primaNotaLibera!=null)) {
			sb.append("Causale: ")
				.append(causaleEP.getCodice())
				.append(" - registrata il ")
				.append(FormatUtils.formatDate(primaNotaLibera.getDataRegistrazione()));
		}
		return sb.toString();
	}
	
	/**
	 * Calcolo della stringa della data richiesta.
	 * 
	 * @return la stringa della data richiesta
	 */
	protected String computaStringaDescrizionePrimaNota () {
		StringBuilder sb = new StringBuilder();
		if(primaNotaLibera!=null) {
			sb.append("Descrizione ")
				.append(primaNotaLibera.getDescrizione());
		}
		return sb.toString();
	}

	/**
	 * Calcolo della stringa  titolo della pagina di riepilogo/consultazione
	 * 
	 * @return la stringa della data richiesta
	 */
	protected String computaTitoloStep3PrimaNota () {
		StringBuilder sb = new StringBuilder();
		sb.append("Prima Nota ");
		if(primaNotaLibera!=null) {
			if (StatoOperativoPrimaNota.PROVVISORIO.equals(primaNotaLibera.getStatoOperativoPrimaNota())){
				sb.append(" provvisoria N.")
				.append(primaNotaLibera.getNumero());
			} else if (StatoOperativoPrimaNota.DEFINITIVO.equals(primaNotaLibera.getStatoOperativoPrimaNota())){
				sb.append(" definitiva N.")
				.append(primaNotaLibera.getNumeroRegistrazioneLibroGiornale());
			}
		}
		return sb.toString();
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaModulareCausale}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaModulareCausale creaRequestRicercaSinteticaModulareCausale() {
		RicercaSinteticaModulareCausale request = creaRequest(RicercaSinteticaModulareCausale.class);
		CausaleEP causEpPerRequest = new CausaleEP();
		
		request.setBilancio(getBilancio());
		// Limitare?
		request.setParametriPaginazione(creaParametriPaginazione(Integer.MAX_VALUE));
		// TODO: verificare quali siano quelle adatte
		request.setCausaleEPModelDetails();
		
		if (evento != null) {
			request.setTipoEvento(evento.getTipoEvento());
		}
		
		causEpPerRequest.setTipoCausale(getTipoCausale());
		causEpPerRequest.setAmbito(getAmbito());
		request.setCausaleEP(causEpPerRequest);
		
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaConto}.
	 * @param c il conto per cui effettuare la ricerca
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaConto creaRequestRicercaSinteticaConto(Conto c) {
		RicercaSinteticaConto request = creaRequest(RicercaSinteticaConto.class);
		request.setBilancio(getBilancio()); 
		request.setConto(c);
		request.getConto().setAmbito(getAmbito());
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaPrimeNote}.
	 * 
	 * @return la request creata
	 */
	public RicercaPrimeNote creaRequestRicercaPrimeNote() {
		RicercaPrimeNote request = creaRequest(RicercaPrimeNote.class);
		primaNotaDaCollegare.setAmbito(getAmbito());
		request.setAnnoPrimaNota(annoPrimaNota);
		request.setPrimaNota(primaNotaDaCollegare);
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioConto}.
	 * @param c il conto per cui effettuare la ricerca
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioConto creaRequestRicercaDettaglioConto(Conto c) {
		RicercaDettaglioConto request = creaRequest(RicercaDettaglioConto.class);
		request.setBilancio(getBilancio());
		request.setConto(c);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link ValidaPrimaNota}.
	 *
	 * @return la request creata
	 */
	public ValidaPrimaNota creaRequestValidaPrimaNota( ) {
		ValidaPrimaNota request = creaRequest(ValidaPrimaNota.class);
		
		request.setPrimaNota(getPrimaNotaLibera());

		return request;
	}
	
	
	/**
	 * Crea la request per il servizio ricerca codifiche per tipoCodifica uguale a ClassePiano
	 * @return la request
	 */
	public RicercaCodifiche creaRequestRicercaClassi(){
		return creaRequestRicercaCodifiche("ClassePiano_" + getAmbitoSuffix());
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioModulareCausale}.
	 * @return la request creata
	 */
	public RicercaDettaglioModulareCausale creaRequestRicercaDettaglioModulareCausale() {
		RicercaDettaglioModulareCausale req = creaRequest(RicercaDettaglioModulareCausale.class);
		
		req.setBilancio(getBilancio());
		req.setCausaleEP(getCausaleEP());
		// Valutare se servono altri
		req.setCausaleEPModelDetails(CausaleEPModelDetail.Conto,CausaleEPModelDetail.Tipo);
		
		return req;
	}


}
