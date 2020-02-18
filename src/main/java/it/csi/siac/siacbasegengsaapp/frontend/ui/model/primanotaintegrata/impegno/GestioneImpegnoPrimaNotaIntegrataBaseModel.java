/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.impegno;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinImpegnoHelper;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacfin2app.frontend.ui.util.helper.MovimentoGestioneHelper;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe base di model per la gestione della prima nota integrata sull'impegno.
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 12/10/2015 - gestione GEN/GSA
 */
public abstract class GestioneImpegnoPrimaNotaIntegrataBaseModel extends BaseInserisciAggiornaPrimaNotaIntegrataBaseModel<Impegno, ConsultaRegistrazioneMovFinImpegnoHelper>{

	/** Per la serializzazione */
	private static final long serialVersionUID = -2317708853852023614L;
	
	private Impegno impegno;
	
	/**
	 * @return the impegno
	 */
	public Impegno getImpegno() {
		return impegno;
	}

	/**
	 * @param impegno the impegno to set
	 */
	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}
	
	@Override
	public String getConsultazioneSubpath() {
		return "Impegno";
	}
	
	/**
	 * Alias per l'impegno
	 * @return the movimentoGestione
	 */
	public Impegno getMovimentoGestione() {
		return getImpegno();
	}
	
	/**
	 * @return the datiBaseCapitolo
	 */
	public String getDatiBaseCapitolo() {
		Capitolo<?, ?> capitolo = ottieniCapitolo();
		if(capitolo == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
			.append(capitolo.getNumeroCapitolo())
			.append(" / ")
			.append(capitolo.getNumeroArticolo());
		if(isGestioneUEB()) {
			sb.append(" / ")
				.append(capitolo.getNumeroUEB());
		}
		
		return sb.toString();
	}
	
	/**
	 * @return the datiBaseStrutturaAmministrativoContabileCapitolo
	 */
	public String getDatiBaseStrutturaAmministrativoContabileCapitolo() {
		Capitolo<?, ?> capitolo = ottieniCapitolo();
		if(capitolo == null || capitolo.getStrutturaAmministrativoContabile() == null) {
			return "";
		}
		return new StringBuilder()
			.append(capitolo.getStrutturaAmministrativoContabile().getCodice())
			.append(" - ")
			.append(capitolo.getStrutturaAmministrativoContabile().getDescrizione())
			.toString();
	}
	
	/**
	 * @return the datiBaseTipoFinanziamentoCapitolo
	 */
	public String getDatiBaseTipoFinanziamentoCapitolo() {
		Capitolo<?, ?> capitolo = ottieniCapitolo();
		if(capitolo == null || capitolo.getTipoFinanziamento() == null) {
			return "";
		}
		return new StringBuilder()
			.append(capitolo.getTipoFinanziamento().getCodice())
			.append(" - ")
			.append(capitolo.getTipoFinanziamento().getDescrizione())
			.toString();
	}
	
	/**
	 * @return the datiAttoAmministrativo
	 */
	public String getDatiAttoAmministrativo() {
		AttoAmministrativo attoAmministrativo = ottieniAttoAmministrativo();
		if(attoAmministrativo == null) {
			return "";
		}
		return new StringBuilder()
			.append(attoAmministrativo.getAnno())
			.append(" - ")
			.append(attoAmministrativo.getNumero())
			.toString();
	}
	
	/**
	 * @return the datiStrutturaAmministrativoContabile
	 */
	public String getDatiStrutturaAmministrativoContabile() {
		AttoAmministrativo attoAmministrativo = ottieniAttoAmministrativo();
		if(attoAmministrativo == null || attoAmministrativo.getStrutturaAmmContabile() == null) {
			return "";
		}
		return new StringBuilder()
			.append(attoAmministrativo.getStrutturaAmmContabile().getCodice())
			.append(" - ")
			.append(attoAmministrativo.getStrutturaAmmContabile().getDescrizione())
			.toString();
	}
	
	/**
	 * @return the datiAttoAmministrativoMovimentoGestione
	 */
	public String getDatiAttoAmministrativoMovimentoGestione() {
		if(getImpegno() == null || getImpegno().getAttoAmministrativo() == null) {
			return "";
		}
		return new StringBuilder()
			.append(getImpegno().getAttoAmministrativo().getAnno())
			.append(" - ")
			.append(getImpegno().getAttoAmministrativo().getNumero())
			.toString();
	}
	
	/**
	 * Ottiene il capitolo per il calcolo dei dati.
	 * @return il capitolo della transazione
	 */
	private Capitolo<?, ?> ottieniCapitolo() {
		return getImpegno() != null && getImpegno().getCapitoloUscitaGestione() != null
			? getImpegno().getCapitoloUscitaGestione()
			: null;
	}
	
	/**
	 * Ottiene l'atto amministrativo per il calcolo dei dati.
	 * @return l'atto amministrativo della transazione
	 */
	private AttoAmministrativo ottieniAttoAmministrativo() {
		return getImpegno() != null && getImpegno().getAttoAmministrativo() != null
			? getImpegno().getAttoAmministrativo()
			: null;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiaveOttimizzato}.
	 * 
	 * @param imp l'impegno per cui creare la request
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato(Impegno imp) {
		
		return MovimentoGestioneHelper.creaRequestRicercaImpegnoPerChiaveOttimizzato(getAnnoEsercizioInt(), getEnte(), getRichiedente(), imp);
		
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaContiConciliazionePerClasse}.
	 * 
	 * @param classeDiConciliazione la classeDiConciliazione per cui ricercare i conti di conciliazione
	 * @return la requet creata
	 */
	public RicercaContiConciliazionePerClasse creaRequestRicercaContiConciliazionePerClasse(ClasseDiConciliazione classeDiConciliazione) {
		RicercaContiConciliazionePerClasse req = creaRequest(RicercaContiConciliazionePerClasse.class);
		req.setClasseDiConciliazione(classeDiConciliazione);
		req.setCapitolo(getImpegno().getCapitoloUscitaGestione());
		req.setSoggetto(getImpegno().getSoggetto());
		return req;
	}
}


