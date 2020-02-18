/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.business.utility.DummyMapper;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.ClassificazioneCofogProgramma;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Classe di wrap per il capitolo durante le fasi di variazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 11/12/2013
 * 
 */
public class ElementoCapitoloCodifiche implements Serializable, Cloneable, ModelWrapper {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6033341350873380208L;
	
	private static final Map<String, Class<? extends ClassificatoreGerarchico>> CLASS_MAP;

	private transient LogUtil log = new LogUtil(getClass());

	private Integer uid;
	private Integer annoCapitolo;
	private Integer numeroCapitolo;
	private Integer numeroArticolo;
	private Integer numeroUEB;
	private String descrizioneCapitolo;
	private String descrizioneArticolo;
	private String denominazioneCapitolo;
	private TipoCapitolo tipoCapitolo;
	private CategoriaCapitolo categoriaCapitolo;

	// Ogni singola codifica
	private Missione missione;
	private Programma programma;
	private ClassificazioneCofog classificazioneCofog;
	private TitoloSpesa titoloSpesa;
	private Macroaggregato macroaggregato;
	private SiopeSpesa siopeSpesa;
	private RicorrenteSpesa ricorrenteSpesa;
	private PerimetroSanitarioSpesa perimetroSanitarioSpesa;
	private TransazioneUnioneEuropeaSpesa transazioneUnioneEuropeaSpesa;
	private PoliticheRegionaliUnitarie politicheRegionaliUnitarie;

	private TitoloEntrata titoloEntrata;
	private TipologiaTitolo tipologiaTitolo;
	private CategoriaTipologiaTitolo categoriaTipologiaTitolo;
	private SiopeEntrata siopeEntrata;
	private RicorrenteEntrata ricorrenteEntrata;
	private PerimetroSanitarioEntrata perimetroSanitarioEntrata;
	private TransazioneUnioneEuropeaEntrata transazioneUnioneEuropeaEntrata;

	private ElementoPianoDeiConti elementoPianoDeiConti;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	private TipoFondo tipoFondo;
	private TipoFinanziamento tipoFinanziamento;
	private ClassificatoreGenerico classificatoreGenerico1;
	private ClassificatoreGenerico classificatoreGenerico2;
	private ClassificatoreGenerico classificatoreGenerico3;
	private ClassificatoreGenerico classificatoreGenerico4;
	private ClassificatoreGenerico classificatoreGenerico5;
	private ClassificatoreGenerico classificatoreGenerico6;
	private ClassificatoreGenerico classificatoreGenerico7;
	private ClassificatoreGenerico classificatoreGenerico8;
	private ClassificatoreGenerico classificatoreGenerico9;
	private ClassificatoreGenerico classificatoreGenerico10;
	private ClassificatoreGenerico classificatoreGenerico11;
	private ClassificatoreGenerico classificatoreGenerico12;
	private ClassificatoreGenerico classificatoreGenerico13;
	private ClassificatoreGenerico classificatoreGenerico14;
	private ClassificatoreGenerico classificatoreGenerico15;

	// Ogni singolo campo testuale
	private String descrizioneCodifiche;
	private String note;

	// Ogni singolo flag
	private Boolean flagFondoPluriennaleVincolato;
	private Boolean flagFondoSvalutazioneCrediti;
	private Boolean flagCorsivoPerMemoria;
	private Boolean flagRilevanteIva;
	private Boolean flagFunzioniDelegateRegione;
	
	private Boolean flagImpegnabile;

	// Lista dei figli
	private List<Integer> listaUidCapitolo = new ArrayList<Integer>();
	
	static {
		Map<String, Class<? extends ClassificatoreGerarchico>> temp = new HashMap<String, Class<? extends ClassificatoreGerarchico>>();
		
		temp.put(TipologiaClassificatore.MISSIONE.name(), Missione.class);
		temp.put(TipologiaClassificatore.PROGRAMMA.name(), Programma.class);
		temp.put(TipologiaClassificatore.CLASSIFICAZIONE_COFOG.name(), ClassificazioneCofog.class);
		temp.put(TipologiaClassificatore.DIVISIONE_COFOG.name(), ClassificazioneCofog.class);
		temp.put(TipologiaClassificatore.GRUPPO_COFOG.name(), ClassificazioneCofog.class);
		temp.put(TipologiaClassificatore.TITOLO_SPESA.name(), TitoloSpesa.class);
		temp.put(TipologiaClassificatore.MACROAGGREGATO.name(), Macroaggregato.class);
		temp.put(TipologiaClassificatore.TITOLO_ENTRATA.name(), TitoloEntrata.class);
		temp.put(TipologiaClassificatore.TIPOLOGIA.name(), TipologiaTitolo.class);
		temp.put(TipologiaClassificatore.CATEGORIA.name(), CategoriaTipologiaTitolo.class);
		temp.put(TipologiaClassificatore.PDC.name(), ElementoPianoDeiConti.class);
		temp.put(TipologiaClassificatore.PDC_I.name(), ElementoPianoDeiConti.class);
		temp.put(TipologiaClassificatore.PDC_II.name(), ElementoPianoDeiConti.class);
		temp.put(TipologiaClassificatore.PDC_III.name(), ElementoPianoDeiConti.class);
		temp.put(TipologiaClassificatore.PDC_IV.name(), ElementoPianoDeiConti.class);
		temp.put(TipologiaClassificatore.PDC_V.name(), ElementoPianoDeiConti.class);
		temp.put(TipologiaClassificatore.SIOPE_SPESA.name(), SiopeSpesa.class);
		temp.put(TipologiaClassificatore.SIOPE_SPESA_I.name(), SiopeSpesa.class);
		temp.put(TipologiaClassificatore.SIOPE_SPESA_II.name(), SiopeSpesa.class);
		temp.put(TipologiaClassificatore.SIOPE_SPESA_III.name(), SiopeSpesa.class);
		temp.put(TipologiaClassificatore.SIOPE_ENTRATA.name(), SiopeEntrata.class);
		temp.put(TipologiaClassificatore.SIOPE_ENTRATA_I.name(), SiopeEntrata.class);
		temp.put(TipologiaClassificatore.SIOPE_ENTRATA_II.name(), SiopeEntrata.class);
		temp.put(TipologiaClassificatore.SIOPE_ENTRATA_III.name(), SiopeEntrata.class);
		temp.put(TipologiaClassificatore.CDC.name(), StrutturaAmministrativoContabile.class);
		temp.put(TipologiaClassificatore.CDR.name(), StrutturaAmministrativoContabile.class);
		
		CLASS_MAP = Collections.unmodifiableMap(temp);
	}
	
	/** Costruttore vuoto di default */
	public ElementoCapitoloCodifiche() {
		super();
	}

	@Override
	public int getUid() {
		return uid != null ? uid.intValue() : 0;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(Integer uid) {
		this.uid = uid;
	}

	/**
	 * @return the numeroCapitolo
	 */
	public Integer getNumeroCapitolo() {
		return numeroCapitolo;
	}

	/**
	 * @return the annoCapitolo
	 */
	public Integer getAnnoCapitolo() {
		return annoCapitolo;
	}

	/**
	 * @param annoCapitolo the annoCapitolo to set
	 */
	public void setAnnoCapitolo(Integer annoCapitolo) {
		this.annoCapitolo = annoCapitolo;
	}

	/**
	 * @param numeroCapitolo the numeroCapitolo to set
	 */
	public void setNumeroCapitolo(Integer numeroCapitolo) {
		this.numeroCapitolo = numeroCapitolo;
	}

	/**
	 * @return the numeroArticolo
	 */
	public Integer getNumeroArticolo() {
		return numeroArticolo;
	}

	/**
	 * @param numeroArticolo the numeroArticolo to set
	 */
	public void setNumeroArticolo(Integer numeroArticolo) {
		this.numeroArticolo = numeroArticolo;
	}

	/**
	 * @return the numeroUEB
	 */
	public Integer getNumeroUEB() {
		return numeroUEB;
	}

	/**
	 * @param numeroUEB the numeroUEB to set
	 */
	public void setNumeroUEB(Integer numeroUEB) {
		this.numeroUEB = numeroUEB;
	}

	/**
	 * @return the descrizioneCapitolo
	 */
	public String getDescrizioneCapitolo() {
		return descrizioneCapitolo;
	}

	/**
	 * @param descrizioneCapitolo the descrizioneCapitolo to set
	 */
	public void setDescrizioneCapitolo(String descrizioneCapitolo) {
		this.descrizioneCapitolo = descrizioneCapitolo;
	}

	/**
	 * @return the denominazioneCapitolo
	 */
	public String getDenominazioneCapitolo() {
		return denominazioneCapitolo;
	}

	/**
	 * @param denominazioneCapitolo the denominazioneCapitolo to set
	 */
	public void setDenominazioneCapitolo(String denominazioneCapitolo) {
		this.denominazioneCapitolo = denominazioneCapitolo;
	}

	/**
	 * @return the tipoCapitolo
	 */
	public TipoCapitolo getTipoCapitolo() {
		return tipoCapitolo;
	}

	/**
	 * @param tipoCapitolo the tipoCapitolo to set
	 */
	public void setTipoCapitolo(TipoCapitolo tipoCapitolo) {
		this.tipoCapitolo = tipoCapitolo;
	}

	/**
	 * @return the categoriaCapitolo
	 */
	public CategoriaCapitolo getCategoriaCapitolo() {
		return categoriaCapitolo;
	}

	/**
	 * @param categoriaCapitolo the categoriaCapitolo to set
	 */
	public void setCategoriaCapitolo(CategoriaCapitolo categoriaCapitolo) {
		this.categoriaCapitolo = categoriaCapitolo;
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
	 * @return the classificazioneCofog
	 */
	public ClassificazioneCofog getClassificazioneCofog() {
		return classificazioneCofog;
	}

	/**
	 * @param classificazioneCofog the classificazioneCofog to set
	 */
	public void setClassificazioneCofog(ClassificazioneCofog classificazioneCofog) {
		this.classificazioneCofog = classificazioneCofog;
	}

	/**
	 * @param classificazioneCofogProgramma the classificazioneCofogProgramma to set
	 */
	public void setClassificazioneCofogProgramma(ClassificazioneCofogProgramma classificazioneCofogProgramma) {
		this.classificazioneCofog = classificazioneCofogProgramma;
	}
	
	/**
	 * @return the classificazioneCofogProgramma
	 */
	public ClassificazioneCofogProgramma getClassificazioneCofogProgramma() {
		if(this.classificazioneCofog == null){
			return null;
		}
		ClassificazioneCofogProgramma ccp = new ClassificazioneCofogProgramma();
		DummyMapper.mapNotNullNotEmpty(this.classificazioneCofog, ccp);
		return ccp;
	}

	/**
	 * @return the titoloSpesa
	 */
	public TitoloSpesa getTitoloSpesa() {
		return titoloSpesa;
	}

	/**
	 * @param titoloSpesa the titoloSpesa to set
	 */
	public void setTitoloSpesa(TitoloSpesa titoloSpesa) {
		this.titoloSpesa = titoloSpesa;
	}

	/**
	 * @return the macroaggregato
	 */
	public Macroaggregato getMacroaggregato() {
		return macroaggregato;
	}

	/**
	 * @param macroaggregato the macroaggregato to set
	 */
	public void setMacroaggregato(Macroaggregato macroaggregato) {
		this.macroaggregato = macroaggregato;
	}

	/**
	 * @return the siopeSpesa
	 */
	public SiopeSpesa getSiopeSpesa() {
		return siopeSpesa;
	}

	/**
	 * @param siopeSpesa the siopeSpesa to set
	 */
	public void setSiopeSpesa(SiopeSpesa siopeSpesa) {
		this.siopeSpesa = siopeSpesa;
	}

	/**
	 * @return the ricorrenteSpesa
	 */
	public RicorrenteSpesa getRicorrenteSpesa() {
		return ricorrenteSpesa;
	}

	/**
	 * @param ricorrenteSpesa the ricorrenteSpesa to set
	 */
	public void setRicorrenteSpesa(RicorrenteSpesa ricorrenteSpesa) {
		this.ricorrenteSpesa = ricorrenteSpesa;
	}

	/**
	 * @return the perimetroSanitarioSpesa
	 */
	public PerimetroSanitarioSpesa getPerimetroSanitarioSpesa() {
		return perimetroSanitarioSpesa;
	}

	/**
	 * @param perimetroSanitarioSpesa the perimetroSanitarioSpesa to set
	 */
	public void setPerimetroSanitarioSpesa(PerimetroSanitarioSpesa perimetroSanitarioSpesa) {
		this.perimetroSanitarioSpesa = perimetroSanitarioSpesa;
	}

	/**
	 * @return the transazioneUnioneEuropeaSpesa
	 */
	public TransazioneUnioneEuropeaSpesa getTransazioneUnioneEuropeaSpesa() {
		return transazioneUnioneEuropeaSpesa;
	}

	/**
	 * @param transazioneUnioneEuropeaSpesa the transazioneUnioneEuropeaSpesa to set
	 */
	public void setTransazioneUnioneEuropeaSpesa(TransazioneUnioneEuropeaSpesa transazioneUnioneEuropeaSpesa) {
		this.transazioneUnioneEuropeaSpesa = transazioneUnioneEuropeaSpesa;
	}

	/**
	 * @return the politicheRegionaliUnitarie
	 */
	public PoliticheRegionaliUnitarie getPoliticheRegionaliUnitarie() {
		return politicheRegionaliUnitarie;
	}

	/**
	 * @param politicheRegionaliUnitarie the politicheRegionaliUnitarie to set
	 */
	public void setPoliticheRegionaliUnitarie(PoliticheRegionaliUnitarie politicheRegionaliUnitarie) {
		this.politicheRegionaliUnitarie = politicheRegionaliUnitarie;
	}

	/**
	 * @return the titoloEntrata
	 */
	public TitoloEntrata getTitoloEntrata() {
		return titoloEntrata;
	}

	/**
	 * @param titoloEntrata the titoloEntrata to set
	 */
	public void setTitoloEntrata(TitoloEntrata titoloEntrata) {
		this.titoloEntrata = titoloEntrata;
	}

	/**
	 * @return the tipologiaTitolo
	 */
	public TipologiaTitolo getTipologiaTitolo() {
		return tipologiaTitolo;
	}

	/**
	 * @param tipologiaTitolo the tipologiaTitolo to set
	 */
	public void setTipologiaTitolo(TipologiaTitolo tipologiaTitolo) {
		this.tipologiaTitolo = tipologiaTitolo;
	}

	/**
	 * @return the categoriaTipologiaTitolo
	 */
	public CategoriaTipologiaTitolo getCategoriaTipologiaTitolo() {
		return categoriaTipologiaTitolo;
	}

	/**
	 * @param categoriaTipologiaTitolo the categoriaTipologiaTitolo to set
	 */
	public void setCategoriaTipologiaTitolo(CategoriaTipologiaTitolo categoriaTipologiaTitolo) {
		this.categoriaTipologiaTitolo = categoriaTipologiaTitolo;
	}

	/**
	 * @return the siopeEntrata
	 */
	public SiopeEntrata getSiopeEntrata() {
		return siopeEntrata;
	}

	/**
	 * @param siopeEntrata the siopeEntrata to set
	 */
	public void setSiopeEntrata(SiopeEntrata siopeEntrata) {
		this.siopeEntrata = siopeEntrata;
	}

	/**
	 * @return the ricorrenteEntrata
	 */
	public RicorrenteEntrata getRicorrenteEntrata() {
		return ricorrenteEntrata;
	}

	/**
	 * @param ricorrenteEntrata the ricorrenteEntrata to set
	 */
	public void setRicorrenteEntrata(RicorrenteEntrata ricorrenteEntrata) {
		this.ricorrenteEntrata = ricorrenteEntrata;
	}

	/**
	 * @return the perimetroSanitarioEntrata
	 */
	public PerimetroSanitarioEntrata getPerimetroSanitarioEntrata() {
		return perimetroSanitarioEntrata;
	}

	/**
	 * @param perimetroSanitarioEntrata the perimetroSanitarioEntrata to set
	 */
	public void setPerimetroSanitarioEntrata(PerimetroSanitarioEntrata perimetroSanitarioEntrata) {
		this.perimetroSanitarioEntrata = perimetroSanitarioEntrata;
	}

	/**
	 * @return the transazioneUnioneEuropeaEntrata
	 */
	public TransazioneUnioneEuropeaEntrata getTransazioneUnioneEuropeaEntrata() {
		return transazioneUnioneEuropeaEntrata;
	}

	/**
	 * @param transazioneUnioneEuropeaEntrata the transazioneUnioneEuropeaEntrata to set
	 */
	public void setTransazioneUnioneEuropeaEntrata(TransazioneUnioneEuropeaEntrata transazioneUnioneEuropeaEntrata) {
		this.transazioneUnioneEuropeaEntrata = transazioneUnioneEuropeaEntrata;
	}

	/**
	 * @return the elementoPianoDeiConti
	 */
	public ElementoPianoDeiConti getElementoPianoDeiConti() {
		return elementoPianoDeiConti;
	}

	/**
	 * @param elementoPianoDeiConti the elementoPianoDeiConti to set
	 */
	public void setElementoPianoDeiConti(ElementoPianoDeiConti elementoPianoDeiConti) {
		this.elementoPianoDeiConti = elementoPianoDeiConti;
	}

	/**
	 * @return the strutturaAmministrativoContabile
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}

	/**
	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
	 */
	public void setStrutturaAmministrativoContabile(StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}

	/**
	 * @return the tipoFondo
	 */
	public TipoFondo getTipoFondo() {
		return tipoFondo;
	}

	/**
	 * @param tipoFondo the tipoFondo to set
	 */
	public void setTipoFondo(TipoFondo tipoFondo) {
		this.tipoFondo = tipoFondo;
	}

	/**
	 * @return the tipoFinanziamento
	 */
	public TipoFinanziamento getTipoFinanziamento() {
		return tipoFinanziamento;
	}

	/**
	 * @param tipoFinanziamento the tipoFinanziamento to set
	 */
	public void setTipoFinanziamento(TipoFinanziamento tipoFinanziamento) {
		this.tipoFinanziamento = tipoFinanziamento;
	}

	/**
	 * @return the classificatoreGenerico1
	 */
	public ClassificatoreGenerico getClassificatoreGenerico1() {
		return classificatoreGenerico1;
	}

	/**
	 * @param classificatoreGenerico1 the classificatoreGenerico1 to set
	 */
	public void setClassificatoreGenerico1(ClassificatoreGenerico classificatoreGenerico1) {
		this.classificatoreGenerico1 = classificatoreGenerico1;
	}

	/**
	 * @return the classificatoreGenerico2
	 */
	public ClassificatoreGenerico getClassificatoreGenerico2() {
		return classificatoreGenerico2;
	}

	/**
	 * @param classificatoreGenerico2 the classificatoreGenerico2 to set
	 */
	public void setClassificatoreGenerico2(ClassificatoreGenerico classificatoreGenerico2) {
		this.classificatoreGenerico2 = classificatoreGenerico2;
	}

	/**
	 * @return the classificatoreGenerico3
	 */
	public ClassificatoreGenerico getClassificatoreGenerico3() {
		return classificatoreGenerico3;
	}

	/**
	 * @param classificatoreGenerico3 the classificatoreGenerico3 to set
	 */
	public void setClassificatoreGenerico3(ClassificatoreGenerico classificatoreGenerico3) {
		this.classificatoreGenerico3 = classificatoreGenerico3;
	}

	/**
	 * @return the classificatoreGenerico4
	 */
	public ClassificatoreGenerico getClassificatoreGenerico4() {
		return classificatoreGenerico4;
	}

	/**
	 * @param classificatoreGenerico4 the classificatoreGenerico4 to set
	 */
	public void setClassificatoreGenerico4(ClassificatoreGenerico classificatoreGenerico4) {
		this.classificatoreGenerico4 = classificatoreGenerico4;
	}

	/**
	 * @return the classificatoreGenerico5
	 */
	public ClassificatoreGenerico getClassificatoreGenerico5() {
		return classificatoreGenerico5;
	}

	/**
	 * @param classificatoreGenerico5 the classificatoreGenerico5 to set
	 */
	public void setClassificatoreGenerico5(ClassificatoreGenerico classificatoreGenerico5) {
		this.classificatoreGenerico5 = classificatoreGenerico5;
	}

	/**
	 * @return the classificatoreGenerico6
	 */
	public ClassificatoreGenerico getClassificatoreGenerico6() {
		return classificatoreGenerico6;
	}

	/**
	 * @param classificatoreGenerico6 the classificatoreGenerico6 to set
	 */
	public void setClassificatoreGenerico6(ClassificatoreGenerico classificatoreGenerico6) {
		this.classificatoreGenerico6 = classificatoreGenerico6;
	}

	/**
	 * @return the classificatoreGenerico7
	 */
	public ClassificatoreGenerico getClassificatoreGenerico7() {
		return classificatoreGenerico7;
	}

	/**
	 * @param classificatoreGenerico7 the classificatoreGenerico7 to set
	 */
	public void setClassificatoreGenerico7(ClassificatoreGenerico classificatoreGenerico7) {
		this.classificatoreGenerico7 = classificatoreGenerico7;
	}

	/**
	 * @return the classificatoreGenerico8
	 */
	public ClassificatoreGenerico getClassificatoreGenerico8() {
		return classificatoreGenerico8;
	}

	/**
	 * @param classificatoreGenerico8 the classificatoreGenerico8 to set
	 */
	public void setClassificatoreGenerico8(ClassificatoreGenerico classificatoreGenerico8) {
		this.classificatoreGenerico8 = classificatoreGenerico8;
	}

	/**
	 * @return the classificatoreGenerico9
	 */
	public ClassificatoreGenerico getClassificatoreGenerico9() {
		return classificatoreGenerico9;
	}

	/**
	 * @param classificatoreGenerico9 the classificatoreGenerico9 to set
	 */
	public void setClassificatoreGenerico9(ClassificatoreGenerico classificatoreGenerico9) {
		this.classificatoreGenerico9 = classificatoreGenerico9;
	}

	/**
	 * @return the classificatoreGenerico10
	 */
	public ClassificatoreGenerico getClassificatoreGenerico10() {
		return classificatoreGenerico10;
	}

	/**
	 * @param classificatoreGenerico10 the classificatoreGenerico10 to set
	 */
	public void setClassificatoreGenerico10(ClassificatoreGenerico classificatoreGenerico10) {
		this.classificatoreGenerico10 = classificatoreGenerico10;
	}

	/**
	 * @return the classificatoreGenerico11
	 */
	public ClassificatoreGenerico getClassificatoreGenerico11() {
		return classificatoreGenerico11;
	}

	/**
	 * @param classificatoreGenerico11 the classificatoreGenerico11 to set
	 */
	public void setClassificatoreGenerico11(ClassificatoreGenerico classificatoreGenerico11) {
		this.classificatoreGenerico11 = classificatoreGenerico11;
	}

	/**
	 * @return the classificatoreGenerico12
	 */
	public ClassificatoreGenerico getClassificatoreGenerico12() {
		return classificatoreGenerico12;
	}

	/**
	 * @param classificatoreGenerico12 the classificatoreGenerico12 to set
	 */
	public void setClassificatoreGenerico12(ClassificatoreGenerico classificatoreGenerico12) {
		this.classificatoreGenerico12 = classificatoreGenerico12;
	}

	/**
	 * @return the classificatoreGenerico13
	 */
	public ClassificatoreGenerico getClassificatoreGenerico13() {
		return classificatoreGenerico13;
	}

	/**
	 * @param classificatoreGenerico13 the classificatoreGenerico13 to set
	 */
	public void setClassificatoreGenerico13(ClassificatoreGenerico classificatoreGenerico13) {
		this.classificatoreGenerico13 = classificatoreGenerico13;
	}

	/**
	 * @return the classificatoreGenerico14
	 */
	public ClassificatoreGenerico getClassificatoreGenerico14() {
		return classificatoreGenerico14;
	}

	/**
	 * @param classificatoreGenerico14 the classificatoreGenerico14 to set
	 */
	public void setClassificatoreGenerico14(ClassificatoreGenerico classificatoreGenerico14) {
		this.classificatoreGenerico14 = classificatoreGenerico14;
	}

	/**
	 * @return the classificatoreGenerico15
	 */
	public ClassificatoreGenerico getClassificatoreGenerico15() {
		return classificatoreGenerico15;
	}

	/**
	 * @param classificatoreGenerico15 the classificatoreGenerico15 to set
	 */
	public void setClassificatoreGenerico15(ClassificatoreGenerico classificatoreGenerico15) {
		this.classificatoreGenerico15 = classificatoreGenerico15;
	}

	/**
	 * @return the descrizioneCodifiche
	 */
	public String getDescrizioneCodifiche() {
		return descrizioneCodifiche;
	}

	/**
	 * @param descrizioneCodifiche the descrizioneCodifiche to set
	 */
	public void setDescrizioneCodifiche(String descrizioneCodifiche) {
		this.descrizioneCodifiche = descrizioneCodifiche;
	}

	/**
	 * @return the descrizioneArticolo
	 */
	public String getDescrizioneArticolo() {
		return descrizioneArticolo;
	}

	/**
	 * @param descrizioneArticolo the descrizioneArticolo to set
	 */
	public void setDescrizioneArticolo(String descrizioneArticolo) {
		this.descrizioneArticolo = descrizioneArticolo;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the flagFondoPluriennaleVincolato
	 */
	public Boolean getFlagFondoPluriennaleVincolato() {
		return flagFondoPluriennaleVincolato;
	}

	/**
	 * @param flagFondoPluriennaleVincolato the flagFondoPluriennaleVincolato to set
	 */
	public void setFlagFondoPluriennaleVincolato(Boolean flagFondoPluriennaleVincolato) {
		this.flagFondoPluriennaleVincolato = flagFondoPluriennaleVincolato;
	}

	/**
	 * @return the flagFondoSvalutazioneCrediti
	 */
	public Boolean getFlagFondoSvalutazioneCrediti() {
		return flagFondoSvalutazioneCrediti;
	}

	/**
	 * @param flagFondoSvalutazioneCrediti the flagFondoSvalutazioneCrediti to set
	 */
	public void setFlagFondoSvalutazioneCrediti(Boolean flagFondoSvalutazioneCrediti) {
		this.flagFondoSvalutazioneCrediti = flagFondoSvalutazioneCrediti;
	}

	/**
	 * @return the flagCorsivoPerMemoria
	 */
	public Boolean getFlagCorsivoPerMemoria() {
		return flagCorsivoPerMemoria;
	}

	/**
	 * @param flagCorsivoPerMemoria the flagCorsivoPerMemoria to set
	 */
	public void setFlagCorsivoPerMemoria(Boolean flagCorsivoPerMemoria) {
		this.flagCorsivoPerMemoria = flagCorsivoPerMemoria;
	}

	/**
	 * @return the flagRilevanteIva
	 */
	public Boolean getFlagRilevanteIva() {
		return flagRilevanteIva;
	}

	/**
	 * @param flagRilevanteIva the flagRilevanteIva to set
	 */
	public void setFlagRilevanteIva(Boolean flagRilevanteIva) {
		this.flagRilevanteIva = flagRilevanteIva;
	}

	/**
	 * @return the flagFunzioniDelegateRegione
	 */
	public Boolean getFlagFunzioniDelegateRegione() {
		return flagFunzioniDelegateRegione;
	}

	/**
	 * @param flagFunzioniDelegateRegione the flagFunzioniDelegateRegione to set
	 */
	public void setFlagFunzioniDelegateRegione(Boolean flagFunzioniDelegateRegione) {
		this.flagFunzioniDelegateRegione = flagFunzioniDelegateRegione;
	}

	/**
	 * @return the flagImpegnabile
	 */
	public Boolean getFlagImpegnabile() {
		return flagImpegnabile;
	}

	/**
	 * @param flagImpegnabile the flagImpegnabile to set
	 */
	public void setFlagImpegnabile(Boolean flagImpegnabile) {
		this.flagImpegnabile = flagImpegnabile;
	}

	/**
	 * @return the listaUidCapitolo
	 */
	public List<Integer> getListaUidCapitolo() {
		return listaUidCapitolo;
	}

	/**
	 * @param listaUidCapitolo the listaUidCapitolo to set
	 */
	public void setListaUidCapitolo(List<Integer> listaUidCapitolo) {
		this.listaUidCapitolo = listaUidCapitolo;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// Per non far lanciare CloneNotSupportedException
		super.clone();
		return SerializationUtils.clone(this);
	}

	/**
	 * Ricostruisce una lista di capitoli data la lista degli uid.
	 * @param <T> la tipizzazione del capitolo
	 * @return la lista dei capitoli corrispondenti agli uid trovati
	 */
	@SuppressWarnings("unchecked")
	public <T extends Capitolo<?, ?>> List<T> reconstructCapitoli() {
		List<T> result = new ArrayList<T>();

		for (Integer i : listaUidCapitolo) {
			try {
				T capitolo = (T) tipoCapitolo.getTipoCapitoloClass().newInstance();
				capitolo.setUid(i);
				result.add(capitolo);
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
		return result;
	}

	/**
	 * Ottiene la lista dei classificatori generici collegati al wrapper.
	 * 
	 * @return la lista dei classificatori generici
	 */
	public List<ClassificatoreGenerico> ottieniListaClassificatoriGenerici() {
		List<ClassificatoreGenerico> result = new ArrayList<ClassificatoreGenerico>();

		addIfNotNullAndValid(result, ricorrenteSpesa);
		addIfNotNullAndValid(result, perimetroSanitarioSpesa);
		addIfNotNullAndValid(result, transazioneUnioneEuropeaSpesa);
		addIfNotNullAndValid(result, politicheRegionaliUnitarie);

		addIfNotNullAndValid(result, ricorrenteEntrata);
		addIfNotNullAndValid(result, perimetroSanitarioEntrata);
		addIfNotNullAndValid(result, transazioneUnioneEuropeaEntrata);

		addIfNotNullAndValid(result, tipoFondo);
		addIfNotNullAndValid(result, tipoFinanziamento);
		addIfNotNullAndValid(result, classificatoreGenerico1);
		addIfNotNullAndValid(result, classificatoreGenerico2);
		addIfNotNullAndValid(result, classificatoreGenerico3);
		addIfNotNullAndValid(result, classificatoreGenerico4);
		addIfNotNullAndValid(result, classificatoreGenerico5);
		addIfNotNullAndValid(result, classificatoreGenerico6);
		addIfNotNullAndValid(result, classificatoreGenerico7);
		addIfNotNullAndValid(result, classificatoreGenerico8);
		addIfNotNullAndValid(result, classificatoreGenerico9);
		addIfNotNullAndValid(result, classificatoreGenerico10);
		addIfNotNullAndValid(result, classificatoreGenerico11);
		addIfNotNullAndValid(result, classificatoreGenerico12);
		addIfNotNullAndValid(result, classificatoreGenerico13);
		addIfNotNullAndValid(result, classificatoreGenerico14);
		addIfNotNullAndValid(result, classificatoreGenerico15);

		return result;
	}

	/**
	 * Ottiene la lista dei classificatori gerarchici collegati al wrapper.
	 * 
	 * @return la lista dei classificatori gerarchici
	 */
	public List<ClassificatoreGerarchico> ottieniListaClassificatoriGerarchici() {
		List<ClassificatoreGerarchico> result = new ArrayList<ClassificatoreGerarchico>();

		// Popolo soltanto i figli
		// Escludo: missione, titoloSpesa, titoloEntrata, tipologiaTitolo
		addIfNotNullAndValid(result, programma);
		addIfNotNullAndValid(result, classificazioneCofog);
		addIfNotNullAndValid(result, macroaggregato);
		addIfNotNullAndValid(result, siopeSpesa);

		addIfNotNullAndValid(result, categoriaTipologiaTitolo);
		addIfNotNullAndValid(result, siopeEntrata);

		addIfNotNullAndValid(result, elementoPianoDeiConti);
		addIfNotNullAndValid(result, strutturaAmministrativoContabile);

		return result;
	}

	/**
	 * Aggiunge una classificatore generico al wrapper.
	 * @param classificatore il classificatore da aggiungere
	 */
	public void addClassificatoreGenerico(ClassificatoreGenerico classificatore) {
		Class<?> clazz = classificatore.getClass();
		StringBuilder classificatoreClassName = new StringBuilder().append(classificatore.getClass().getSimpleName());
		if (ClassificatoreGenerico.class.equals(clazz)) {
			// Ho ancora necessita' di capire che tipo di classificatore sono (Classificatore1 - Classificatore10)
			TipoClassificatore tc = classificatore.getTipoClassificatore();
			String[] codices = tc.getCodice().split("_");
			if (codices.length > 1) {
				int value = Integer.parseInt(codices[1]);
				if(value > 35) {
					// 36-50 => 1-15 di entrata
					value -= 35;
				} else if(value > 30) {
					// 31-35 => 11-15 di spesa
					value -= 20;
				}
				classificatoreClassName.append(value);
			}
		}

		try {
			Method metodo = getClass().getMethod("set" + classificatoreClassName.toString(), clazz);
			metodo.invoke(this, classificatore);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			log.debug("addClassificatoreGenerico", "Non presente classificatore di tipo " + classificatoreClassName);
		}
	}

	/**
	 * Aggiunge una classificatore gerarchico al wrapper.
	 * 
	 * @param classificatore il classificatore da aggiungere
	 */
	public void addClassificatoreGerarchico(ClassificatoreGerarchico classificatore) {
		ClassificatoreGerarchico classificatoreUtilizzato = classificatore;
		Class<?> clazz = classificatore.getClass();
		if (ClassificatoreGerarchico.class.equals(clazz)) {
			// Non va bene: ottengo la classe a partire dal tipo di classificatore
			clazz = fromTipoClassificatore(classificatore.getTipoClassificatore());
			try {
				classificatoreUtilizzato = (ClassificatoreGerarchico) clazz.newInstance();
			} catch (Exception e) {
				throw new UnsupportedOperationException(e);
			}
			classificatoreUtilizzato.setUid(classificatore.getUid());
			classificatoreUtilizzato.setCodice(classificatore.getCodice());
			classificatoreUtilizzato.setDescrizione(classificatore.getDescrizione());
		}

		// Injetto solo la codifica figlia
		if (classificatore instanceof ElementoPianoDeiConti
				|| classificatore instanceof StrutturaAmministrativoContabile
				|| classificatore instanceof SiopeEntrata
				|| classificatore instanceof SiopeSpesa
				|| classificatore instanceof ClassificazioneCofog) {
			injettaFoglia(classificatore);
			return;
		}

		String classificatoreClassName = classificatoreUtilizzato.getClass().getSimpleName();

		try {
			Method metodo = getClass().getMethod("set" + classificatoreClassName, clazz);
			metodo.invoke(this, classificatoreUtilizzato);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			log.debug("addClassificatoreGerarchico", "Non presente classificatore di tipo " + classificatoreClassName);
		}
	}

	/**
	 * Injetta l'ultima foglia di una famiglia di classificatori gerarchici all'interno del wrapper
	 * @param classificatore il classificatore da injettare
	 */
	private void injettaFoglia(ClassificatoreGerarchico classificatore) {
		Class<?> clazz = classificatore.getClass();
		String className = clazz.getSimpleName();
		Method metodoGet = ReflectionUtils.findMethod(ElementoCapitoloCodifiche.class, "get" + className);
		ClassificatoreGerarchico classificatoreGiaPresente = (ClassificatoreGerarchico) ReflectionUtils.invokeMethod(metodoGet, this);
		if (classificatoreGiaPresente == null || classificatoreGiaPresente.getLivello() < classificatore.getLivello()) {
			Method metodoSet = ReflectionUtils.findMethod(ElementoCapitoloCodifiche.class, "set" + className, clazz);
			ReflectionUtils.invokeMethod(metodoSet, this, classificatore);
		}
	}
	
	/**
	 * Ottiene la classe del classificatore dal tipo di classificatore.
	 * 
	 * @param tipoClassificatore il tipo da cui inferire la classe
	 * 
	 * @return la classe relativa al tipo di classificatore
	 */
	private Class<? extends ClassificatoreGerarchico> fromTipoClassificatore(TipoClassificatore tipoClassificatore) {
		String codiceTipo = tipoClassificatore.getCodice();
		Class<? extends ClassificatoreGerarchico> clazz = CLASS_MAP.get(codiceTipo);
		return clazz == null ? ClassificatoreGerarchico.class : clazz;
	}

	/**
	 * Aggiunge un elemento alla lista se &eacute; non-<code>null</code>.
	 * 
	 * @param lista la lista da popolare
	 * @param elemento l'elemento da apporre alla lista
	 */
	private void addIfNotNullAndValid(List<ClassificatoreGenerico> lista, ClassificatoreGenerico elemento) {
		if (elemento != null && elemento.getUid() != 0) {
			lista.add(elemento);
		}
	}

	/**
	 * Aggiunge un elemento alla lista se &eacute; non-<code>null</code>.
	 * 
	 * @param lista la lista da popolare
	 * @param elemento l'elemento da apporre alla lista
	 */
	private void addIfNotNullAndValid(List<ClassificatoreGerarchico> lista, ClassificatoreGerarchico elemento) {
		if (elemento != null && elemento.getUid() != 0) {
			lista.add(elemento);
		}
	}

	/**
	 * Imposta la denominazione per il presente capitolo
	 */
	public void impostaDenominazioneCapitolo() {
		boolean capitoloDiUscita = TipoCapitolo.CAPITOLO_USCITA_PREVISIONE.equals(tipoCapitolo) || TipoCapitolo.CAPITOLO_USCITA_GESTIONE.equals(tipoCapitolo);
		String denominazioneDaInjettare = (capitoloDiUscita ? "S" : "E") + "-" + annoCapitolo + "/" + numeroCapitolo + "/" + numeroArticolo;
		denominazioneCapitolo = denominazioneDaInjettare;
	}

	/**
	 * Imposta la descrizione delle codifiche per il presente capitolo
	 */
	public void impostaDescrizioneCodifiche() {
		boolean capitoloDiUscita = TipoCapitolo.CAPITOLO_USCITA_PREVISIONE.equals(tipoCapitolo) || TipoCapitolo.CAPITOLO_USCITA_GESTIONE.equals(tipoCapitolo);
		List<String> list = new ArrayList<String>();
		if (capitoloDiUscita) {
			addCodiceCodifica(titoloSpesa, "Titolo", list);
			addCodiceCodifica(missione, "Missione", list);
			addCodiceCodifica(programma, "Programma", list);
			addCodiceCodifica(macroaggregato, "Macroaggregato", list);
		} else {
			addCodiceCodifica(titoloEntrata, "Titolo", list);
			addCodiceCodifica(tipologiaTitolo, "Tipologia", list);
			addCodiceCodifica(categoriaTipologiaTitolo, "Categoria", list);
		}
		descrizioneCodifiche = StringUtils.join(list, ",");
	}
	
	/**
	 * Aggiunge il codice della codifica
	 * @param c la codifica
	 * @param nome il nome
	 * @param list la lista
	 */
	private void addCodiceCodifica(Codifica c, String nome, List<String> list) {
		if(c == null) {
			return;
		}
		list.add(nome + ":" + c.getCodice());
	}

	/**
	 * Effettua una validazione delle codifiche presenti nel wrapper.
	 * @param bilancio il bilancio in corso
	 * 
	 * @return <code>true</code> se le codifiche sono valide; <code>false</code> altrimenti
	 */
	public boolean validaCodifiche(Bilancio bilancio) {
		String methodName = "validaCodifiche";
		boolean result;
		if(!BilConstants.CODICE_CATEGORIA_CAPITOLO_STANDARD.getConstant().equals(categoriaCapitolo.getCodice())){
			log.debug(methodName, "il tipo non e' STD, non devo validare");
			return true;
		}
		Date now = ottieniDataOdiernaOFineBilancio(bilancio);
		if (TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE.equals(tipoCapitolo) || TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE.equals(tipoCapitolo)) {
			result = validaCodificheEntrata(now);
		} else {
			result = validaCodificheUscita(now);
		}
		result = result && validaCodifica(elementoPianoDeiConti, now) && validaCodifica(strutturaAmministrativoContabile, now);
		return result;
	}
	
	/**
	 * Confronta l'anno corrente con l'anno di bilancio. Se questi sono uguali, restituisce la data odierna. In caso contrario, restituisce la data 30-12-annoBilancio.
	 * @param bilancio il bilancio
	 * @return la data
	 * */
	protected Date ottieniDataOdiernaOFineBilancio(Bilancio bilancio){
		final String methodName = "ottieniDataDaConfrontare" ;
		//SIAC-4365
		if (bilancio == null){
			return new Date();
		}
		
		Calendar gc = Calendar.getInstance();
		int currentYear = gc.get(Calendar.YEAR);
		int annoBilancio = bilancio.getAnno();
		if(currentYear > annoBilancio) {
			log.debug(methodName, "Anno corrente (" + currentYear + ") > anno del bilancio (" + annoBilancio + "): impostazione della data a 31/12/" + annoBilancio);
			// Se anno corrente > anno di bilancio la data è 30/12/anno di bilancio. non metto l'ultimo giorno dell'anno perche' voglio considerare le codifiche che 
			//hanno data validita pari all'ultimo dell'anno
			gc.set(annoBilancio, Calendar.DECEMBER, 30, 0, 0);
			return gc.getTime();
		}
		log.debug(methodName, "Anno corrente (" + currentYear + ") = anno del bilancio (" + annoBilancio + "): impostazione della data corrente");
		return new Date();
		
	}

	/**
	 * Valido le codifiche di entrata.
	 * 
	 * @param now adesso
	 * 
	 * @return <code>true</code> se le codifiche sono valide; <code>false</code> in caso contrario
	 */
	private boolean validaCodificheEntrata(Date now) {
		return validaCodifica(titoloEntrata, now) && validaCodifica(tipologiaTitolo, now) && validaCodifica(categoriaTipologiaTitolo, now);
	}

	/**
	 * Valido le codifiche di spesa.
	 * 
	 * @param now adesso
	 * 
	 * @return <code>true</code> se le codifiche sono valide; <code>false</code> in caso contrario
	 */
	private boolean validaCodificheUscita(Date now) {
		return validaCodifica(missione, now) && validaCodifica(programma, now) && validaCodifica(titoloSpesa, now) && validaCodifica(macroaggregato, now);
	}

	/**
	 * Valida la presenza di una determinata codifica.
	 * 
	 * @param codifica la codifica da validare
	 * @param now adesso
	 * 
	 * @return <code>true</code> se la codifica &eacute; valida; <code>false</code> in caso contrario
	 */
	private boolean validaCodifica(Codifica codifica, Date now) {
		return codifica != null && codifica.getUid() != 0 && 
				(codifica.getDataFineValidita() == null || codifica.getDataFineValidita().after(now));
	}
	
	/**
	 * Per la deserializzazione.
	 * 
	 * @param in lo stream in ingresso
	 * 
	 * @throws IOException            in caso di eccezione di I/O
	 * @throws ClassNotFoundException nel caso in cui il Classloader non trovi la classe
	 */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		log = new LogUtil(getClass());
	}

}
