/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.causali;

import java.util.Arrays;
import java.util.List;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.RicercaCausaleEPBaseModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausale;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;

/**
 * Classe di model per la ricerca della causale EP.
 * 
 * @author Simona Paggio
 * @version 1.0.0 - 07/05/2015
 *
 */
public class RicercaCausaleEPGSAModel extends RicercaCausaleEPBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -332114149548408690L;
	
	//CR-4596
	private ClasseDiConciliazione classeDiConciliazione;
	
	/** Costruttore vuoto di default */
	public RicercaCausaleEPGSAModel() {
		setTitolo("Ricerca Causale");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
	/**
	 * @return the classiDiConciliazione
	 */
	public List<ClasseDiConciliazione> getListaClassiDiConciliazione() {
		return Arrays.asList(ClasseDiConciliazione.values());
	}

	/**
	 * @return the classeDiConciliazione
	 */
	public ClasseDiConciliazione getClasseDiConciliazione() {
		return classeDiConciliazione;
	}

	/**
	 * @param classeDiConciliazione the classeDiConciliazione to sety
	 */
	public void setClasseDiConciliazione(ClasseDiConciliazione classeDiConciliazione) {
		this.classeDiConciliazione = classeDiConciliazione;
	}
	
	@Override
	protected void impostaContoTipoOperazione(RicercaSinteticaCausale request) {
		if((getConto() == null || getConto().getUid() == 0) && getClasseDiConciliazione() == null){
			//non e' stato impostato un conto ne' una classe di conciliazione come parametro di ricerca
			return;
		}
		// Impostazione conto
		ContoTipoOperazione contoTipoOperazione = new ContoTipoOperazione();
		contoTipoOperazione.setConto(getConto());
		contoTipoOperazione.setClasseDiConciliazione(getClasseDiConciliazione());
		getCausaleEP().addContoTipoOperazione(contoTipoOperazione);
	}
	
}
