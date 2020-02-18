/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.BaseFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioBaseCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccorser.model.AzioneConsentita;

/**
 * Factory per il wrapping dei Cronoprogrammi per la pagina di aggiornamento.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 11/02/2013
 */
public final class ElementoVersioneCronoprogrammaFactory extends BaseFactory {
	
	private static final String AZIONI_BEGIN = "<div class='btn-group'>" +
			"<button data-toggle='dropdown' class='btn dropdown-toggle'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right'>";
	private static final String AZIONI_AGGIORNA = "<li><a class='pulsanteAggiornaCronoprogramma' href='#'>aggiorna</a></li>";
	private static final String AZIONI_ANNULLA = "<li><a class='pulsanteAnnullaCronoprogramma' href='#'>annulla</a></li>";
	private static final String AZIONI_CONSULTA = "<li><a class='pulsanteConsultaCronoprogramma' href='#'>consulta</a></li>";
	private static final String AZIONI_ASSOCIA_FPV= "<li><a class='pulsanteAssociaCronoprogrammaFPV' href='#'>Scegli per FPV</a></li>";
	private static final String AZIONI_SIMULA_FPV= "<li><a class='pulsanteSimulaCronoprogrammaFPV' href='#'> Salva e simula FPV</a></li>";
	private static final String AZIONI_ANNULLA_SIMULA_FPV= "<li><a class='pulsanteAnnullaSimulaCronoprogrammaFPV' href='#'> annulla simula FPV</a></li>";

	private static final String AZIONI_END = "</ul>" +
		"</div>";
	
	/** Non permettere l'instanziazione della classe */
	private ElementoVersioneCronoprogrammaFactory() {
	}
	
	/**
	 * Crea un'istanza del wrapper a partire dal cronoprogramma.
	 *
	 * @param crono         il cronoprogramma da wrappare
	 * @param annoEsercizio l'anno di esercizio del progetto
	 * @param tipoProgetto the tipo progetto
	 * @return il wrapper creato
	 */
	public static ElementoVersioneCronoprogramma getInstance(Cronoprogramma crono, Integer annoEsercizio, TipoProgetto tipoProgetto) {
		ElementoVersioneCronoprogramma result = new ElementoVersioneCronoprogramma();
		int annoEsercizioInt = annoEsercizio.intValue();
		
		int uid = crono.getUid();
		String versione = crono.getCodice();
		String descrizione = crono.getDescrizione();
		String statoOperativoCronoprogramma = capitaliseString(crono.getStatoOperativoCronoprogramma().name());
		
		WrapperStanziamenti wrapperStanziamentiSpesa = popolaTotaliSpesa(crono, annoEsercizioInt);
		
		WrapperStanziamenti wrapperStanziamentiEntrata = popolaTotaliEntrata(crono, annoEsercizioInt);
		
		Boolean usatoPerFpv = crono.getUsatoPerFpv();
		Boolean daDefinire = crono.getCronoprogrammaDaDefinire();
		BigDecimal quadraturaEntrata = getQuadratura(crono.getCapitoliEntrata());
		BigDecimal quadraturaUscita = getQuadratura(crono.getCapitoliUscita());
		
		result.setUid(uid);
		result.setVersione(versione);
		result.setDescrizione(descrizione);
		result.setStatoOperativoCronoprogramma(statoOperativoCronoprogramma);
		result.setDescrizioneStatoOperativoCronoprogramma(crono.getStatoOperativoCronoprogramma().getDescrizione());
		
		result.setCompetenzaSpesaAnnoPrec(wrapperStanziamentiSpesa.competenzaAnniPrecedenti);
		result.setCompetenzaSpesaAnno0(wrapperStanziamentiSpesa.competenzaAnno0);
		result.setCompetenzaSpesaAnno1(wrapperStanziamentiSpesa.competenzaAnno1);
		result.setCompetenzaSpesaAnno2(wrapperStanziamentiSpesa.competenzaAnno2);
		result.setCompetenzaSpesaAnnoSucc(wrapperStanziamentiSpesa.competenzaAnniSuccessivi);
		
		result.setCompetenzaEntrataAnnoPrec(wrapperStanziamentiEntrata.competenzaAnniPrecedenti);
		result.setCompetenzaEntrataAnno0(wrapperStanziamentiEntrata.competenzaAnno0);         
		result.setCompetenzaEntrataAnno1(wrapperStanziamentiEntrata.competenzaAnno1);         
		result.setCompetenzaEntrataAnno2(wrapperStanziamentiEntrata.competenzaAnno2);         
		result.setCompetenzaEntrataAnnoSucc(wrapperStanziamentiEntrata.competenzaAnniSuccessivi);		
		result.setTipoProgetto(tipoProgetto);
		
		result.setDaDefinire(daDefinire);
		
		
		result.setQuadraturaEntrata(quadraturaEntrata);
		result.setQuadraturaUscita(quadraturaUscita);
		result.setUsatoPerFpv(usatoPerFpv);
		result.setUsatoPerFpvProv(crono.getUsatoPerFpvProv());
		return result;
	}
	
	/**
	 * Crea istanze del wrapper a partire dai cronoprogrammi.
	 *
	 * @param cronoprogrammi la lista dei cronoprogrammi da wrappare
	 * @param annoEsercizio  l'anno di esercizio del progetto
	 * @param tipoProgetto the tipo progetto
	 * @return i wrapper creati
	 */
	public static List<ElementoVersioneCronoprogramma> getInstances(List<Cronoprogramma> cronoprogrammi, Integer annoEsercizio, TipoProgetto tipoProgetto) {
		List<ElementoVersioneCronoprogramma> result = new ArrayList<ElementoVersioneCronoprogramma>();
		
		for(Cronoprogramma crono : cronoprogrammi) {
			result.add(getInstance(crono, annoEsercizio, tipoProgetto));
		}
		return result;
	}
	
	/**
	 * Crea istanze del wrapper a partire dai cronoprogrammi.
	 * 
	 * @param list             la lista dei cronoprogrammi da wrappare
	 * @param annoEsercizio    l'anno di esercizio del progetto
	 * @param azioniConsentite la azioni consentite all'utente
	 * @param tipoProgetto     il tipo di progetto
	 * 
	 * @return i wrapper creati
	 */
	public static List<ElementoVersioneCronoprogramma> getInstances(List<Cronoprogramma> list, Integer annoEsercizio, List<AzioneConsentita> azioniConsentite, TipoProgetto tipoProgetto) {
		List<ElementoVersioneCronoprogramma> result = new ArrayList<ElementoVersioneCronoprogramma>();
		
		for(Cronoprogramma crono : list) {
			ElementoVersioneCronoprogramma instance = getInstance(crono, annoEsercizio, tipoProgetto);
			gestisciAzioni(instance, azioniConsentite);
			result.add(instance);
		}
		
		return result;
	}
	/**
	 * Gestisce le azioni consentite per l'istanza.
	 * 
	 * @param instance         l'istanza 
	 * @param azioniConsentite le azioni consentite all'istanza
	 */
	private static void gestisciAzioni(ElementoVersioneCronoprogramma instance, List<AzioneConsentita> azioniConsentite) {
		Boolean isAggiornaAbilitato = AzioniConsentiteFactory.isAggiornaConsentitoCronoprogrammaNelProgetto(azioniConsentite);
		Boolean isAnnullaAbilitato = AzioniConsentiteFactory.isAnnullaConsentitoCronoprogrammaNelProgetto(azioniConsentite);
		Boolean isConsultaAbilitato = AzioniConsentiteFactory.isConsultaConsentitoCronoprogrammaNelProgetto(azioniConsentite);
		boolean isGestioneFPVProvvisorioConsentita = AzioniConsentiteFactory.isConsentito(AzioniConsentite.CRONOPROGRAMMA_FPV_PROVVISORIO, azioniConsentite);
		//ahmad l'ho aggiunta per evitare che si schianti quando utilizzo i progetti gia inseriti senza il campo usato per fpv
		boolean isUsatoPerFPV = instance.getUsatoPerFpv()!=null && Boolean.TRUE.equals(instance.getUsatoPerFpv());
		//SIAC-6255 : L’azione attualmente presente di 'Scegli per FPV' deve essere abilitata solo per i cronoprogrammi di Previsione.
		boolean isInstanceCorrettaPerFpv = !isUsatoPerFPV && TipoProgetto.PREVISIONE.equals(instance.getTipoProgetto()) && instance.checkStatoOperativoValido();
		boolean isFPVPRovvisorio = instance.getUsatoPerFpvProv()!=null && Boolean.TRUE.equals(instance.getUsatoPerFpvProv()); 
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(AZIONI_BEGIN);
		
		//se non e' settato il campo usatoPerFpv ma il cronoprogramma e' valido -->posso fare aggiornamento eccc..
		//Occorre visualizzare comunque l’azione che avvisa l’utente che il cronoprogramma non è comunque aggiornabile, ma consente di entrare
		if(isAggiornaAbilitato.booleanValue() && !instance.checkStatoOperativoAnnullato()) {
			sb.append(AZIONI_AGGIORNA);
		}
		if(isConsultaAbilitato.booleanValue()) {
			sb.append(AZIONI_CONSULTA);
		}
		if(isAnnullaAbilitato.booleanValue() && !instance.checkStatoOperativoAnnullato()) {
			sb.append(AZIONI_ANNULLA);
		}
		
		if(isInstanceCorrettaPerFpv) {
			sb.append(AZIONI_ASSOCIA_FPV);
		}
		
		if( isGestioneFPVProvvisorioConsentita && isInstanceCorrettaPerFpv && !isFPVPRovvisorio) {
			sb.append(AZIONI_SIMULA_FPV);
		}
		
		if( isGestioneFPVProvvisorioConsentita && isInstanceCorrettaPerFpv && isFPVPRovvisorio) {
			sb.append(AZIONI_ANNULLA_SIMULA_FPV);
		}
		
		sb.append(AZIONI_END);
		
		instance.setAzioni(sb.toString());
	}
	
	/**
	 * Ottiene i valori della quadratura per entrata e spesa.
	 * 
	 * @param list la lista dei dettagli
	 * 
	 * @return il valore della quadratura
	 */
	private static <T extends DettaglioBaseCronoprogramma> BigDecimal getQuadratura(List<T> list) {
		BigDecimal result = BigDecimal.ZERO;
		
		for(T t : list) {
			result = result.add(t.getStanziamento());
		}
		
		return result;
	}
	
	/**
	 * Imposta l'unica azione per il programma di gestione, ovvero la consultazione.
	 * 
	 * @param instance         l'istanza 
	 * @param azioniConsentite le azioni consentite all'istanza
	 */
	public static void impostaAzioniCronoprogrammaGestione(ElementoVersioneCronoprogramma instance, List<AzioneConsentita> azioniConsentite) {
		Boolean isConsultaAbilitato = AzioniConsentiteFactory.isConsultaConsentitoCronoprogrammaNelProgetto(azioniConsentite);
		StringBuilder sb = new StringBuilder();
		
		sb.append(AZIONI_BEGIN);
		if(isConsultaAbilitato.booleanValue()) {
			sb.append(AZIONI_CONSULTA);
		}
		sb.append(AZIONI_END);
		
		instance.setAzioni(sb.toString());
	}
	
	private static WrapperStanziamenti popolaTotaliSpesa(Cronoprogramma crono, int annoEsercizioInt) {
		WrapperStanziamenti wrapperStanziamenti = new WrapperStanziamenti();
		for (DettaglioUscitaCronoprogramma temp : crono.getCapitoliUscita()) {
			if(temp.getAnnoCompetenza() == null) {
				continue;
			}
			addByAnno(annoEsercizioInt, temp,wrapperStanziamenti);
		}
		return wrapperStanziamenti;
	}
	
	private static WrapperStanziamenti popolaTotaliEntrata(Cronoprogramma crono, int annoEsercizioInt) {
		WrapperStanziamenti wrapperStanziamenti = new WrapperStanziamenti();
		for (DettaglioEntrataCronoprogramma temp : crono.getCapitoliEntrata()) {
			if(temp.getAnnoCompetenza() == null) {
				continue;
			}
			addByAnno(annoEsercizioInt,temp,wrapperStanziamenti);
		}
		return wrapperStanziamenti;
	}

	private static void addByAnno(int anno, DettaglioBaseCronoprogramma temp,WrapperStanziamenti wrapperStanziamentiSpesa) {
		int annoCompetenza = temp.getAnnoCompetenza().intValue();
		BigDecimal stanziamento = temp.getStanziamento();
		if(annoCompetenza < anno) {
			wrapperStanziamentiSpesa.addCompetenzaAnniPrecedenti(stanziamento);
			return;
		}
		if(annoCompetenza == anno) {
			wrapperStanziamentiSpesa.addCompetenzaAnno0(stanziamento);
			return;
		}

		anno +=1;		
		if(annoCompetenza == anno) {
			wrapperStanziamentiSpesa.addCompetenzaAnno1(stanziamento);
			return;
		}
		
		anno += 1;
		if(annoCompetenza == anno) {
			wrapperStanziamentiSpesa.addCompetenzaAnno2(stanziamento);
			return;
		}
		
		if(annoCompetenza > anno){
			wrapperStanziamentiSpesa.addCompetenzaAnniSuccessivi(stanziamento);
			return;
		}
		
	}

	
	private static class WrapperStanziamenti {
		BigDecimal competenzaAnno0 = BigDecimal.ZERO;
		BigDecimal competenzaAnno1 = BigDecimal.ZERO;
		BigDecimal competenzaAnno2 = BigDecimal.ZERO;
		BigDecimal competenzaAnniPrecedenti = BigDecimal.ZERO;
		BigDecimal competenzaAnniSuccessivi = BigDecimal.ZERO;
		
		/** Costruttore */
		WrapperStanziamenti() {
			// Constructor giving outer class visibility
		}
		
		/**
		 * Adds the competenza anno 0.
		 *
		 * @param augend the augend
		 */
		public void addCompetenzaAnno0(BigDecimal augend) {
			if(augend == null) {
				return;
			}
			this.competenzaAnno0 = this.competenzaAnno0.add(augend);
		}
		
		/**
		 * Adds the competenza anno 1.
		 *
		 * @param augend the augend
		 */
		public void addCompetenzaAnno1(BigDecimal augend) {
			if(augend == null) {
				return;
			}
			this.competenzaAnno1 = this.competenzaAnno1.add(augend);
		}
		
		/**
		 * Adds the competenza anno 2.
		 *
		 * @param augend the augend
		 */
		public void addCompetenzaAnno2(BigDecimal augend) {
			if(augend == null) {
				return;
			}
			this.competenzaAnno2 = this.competenzaAnno2.add(augend);
		}
		
		/**
		 * Adds the competenza anni precedenti.
		 *
		 * @param augend the augend
		 */
		public void addCompetenzaAnniPrecedenti(BigDecimal augend) {
			if(augend == null) {
				return;
			}
			this.competenzaAnniPrecedenti = this.competenzaAnniPrecedenti.add(augend);
		}
		
		/**
		 * Adds the competenza anni successivi.
		 *
		 * @param augend the augend
		 */
		public void addCompetenzaAnniSuccessivi(BigDecimal augend) {
			if(augend == null) {
				return;
			}
			this.competenzaAnniSuccessivi = this.competenzaAnniSuccessivi.add(augend);
		}
	}
	
}
